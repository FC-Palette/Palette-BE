package fc.server.palette.chat.service;

import fc.server.palette.chat.dto.request.ChatRoomOpenDto;
import fc.server.palette.chat.entity.ChatRoom;
import fc.server.palette.chat.entity.type.ChatRoomType;
import fc.server.palette.chat.repository.ChatRoomRepository;
import fc.server.palette.meeting.dto.request.MeetingCreateDto;
import fc.server.palette.purchase.dto.response.OfferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessageSendingOperations template;

    private static final int PERSONAL_ROOM_MAX_MEMBER = 2;

    @Transactional
    public String openPersonalChatRoom(Long memberId, ChatRoomOpenDto request) {
        Optional<ChatRoom> duplicatedChatRoom = getRequestTypeChatRoom(memberId, request);

        if (duplicatedChatRoom.isPresent()) {
            personalChatRoomMemberResolver(memberId, duplicatedChatRoom.get());
            return duplicatedChatRoom.get().getId();
        }

        ChatRoom newChatRoom = openNewPersonalChatRoom(memberId, request);
        return newChatRoom.getId();
    }

    private Optional<ChatRoom> getRequestTypeChatRoom(Long memberId, ChatRoomOpenDto request) {
        if (request.getType() == ChatRoomType.PERSONAL) {
            return chatRoomRepository.findPersonalChatRoomByEnterList(memberId, request.getParticipant());
        }
        return chatRoomRepository.findSecondHandChatRoomByEnterList(memberId, request.getParticipant(), request.getContentId());
    }

    public void personalChatRoomMemberResolver(Long memberId, ChatRoom personalChatRoom) {
        List<Long> memberList = personalChatRoom.getMemberList();
        Map<Long, LocalDateTime> enterList = personalChatRoom.getEnterList();

        if (memberList.size() == PERSONAL_ROOM_MAX_MEMBER) {
            return;
        }

        Long participant = enterList.keySet().stream()
                .filter(id -> id.equals(memberId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 채팅방입니다."));

        memberList.add(participant);
        enterList.put(participant, LocalDateTime.now());

        chatRoomRepository.save(personalChatRoom);
    }

    private ChatRoom openNewPersonalChatRoom(Long memberId, ChatRoomOpenDto request) {
        ChatRoom chatRoom = new ChatRoom();
        Long opId = request.getParticipant();

        chatRoom.setTitle(memberId + "_" + opId);
        chatRoom.setType(request.getType());
        chatRoom.getMemberList().add(opId);
        chatRoom.getMemberList().add(memberId);
        chatRoom.getEnterList().put(opId, LocalDateTime.now());
        chatRoom.getEnterList().put(memberId, LocalDateTime.now());
        chatRoom.getExitList().put(opId, LocalDateTime.now());
        chatRoom.getExitList().put(memberId, LocalDateTime.now());
        if (request.getType().equals(ChatRoomType.SECONDHAND)) {
            chatRoom.setContentId(request.getContentId());
        }
        return chatRoomRepository.save(chatRoom);
    }

    //그룹톡방 생성
    @Transactional
    public void openGroupChatRoom(Object request, Long memberId, ChatRoomType type) {
        //단체톡방생성에 필요한 요소들 - 타이틀, 호스트, 썸넬, 컨텐트 아이디
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setType(type);
        chatRoom.getMemberList().add(memberId);
        chatRoom.getExitList().put(memberId, LocalDateTime.now());
        chatRoom.getEnterList().put(memberId, LocalDateTime.now());
        if (type.equals(ChatRoomType.PURCHASE)) {
            OfferDto info = (OfferDto) request;
            chatRoom.setTitle(info.getTitle());
            chatRoom.setHost(memberId);
            chatRoom.setContentId(info.getId());
        } else if (type.equals(ChatRoomType.MEETING)) {
            MeetingCreateDto info = (MeetingCreateDto) request;
            chatRoom.setTitle(info.getTitle());
            chatRoom.setHost(memberId);
            chatRoom.setContentId(info.getId());
        }

        chatRoomRepository.save(chatRoom);
    }

    //그룹톡방 참여
    public void participantGroupChatRoom(Long memberId, Long contentId, ChatRoomType type) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByContentIdAndType(contentId, type)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방을 찾을 수 없습니다."));
        chatRoom.getMemberList().add(memberId);
        chatRoom.getEnterList().put(memberId, LocalDateTime.now());
        chatRoom.getExitList().put(memberId, LocalDateTime.now());

        chatRoomRepository.save(chatRoom);
    }

    public void participantGroupChatRoom(Map<Long, Long> results, ChatRoomType type) {
        results.keySet()
                .forEach(key -> participantGroupChatRoom(results.get(key), key, type));
    }

    @Transactional
    public void validateRoomIdAndMemberId(String roomId, Long memberId) {
        chatRoomRepository.findChatRoomByIdAndMemberId(roomId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방을 찾을 수 없습니다."));
    }

    //개인 톡방 조회
    @Transactional
    public List<ChatRoom> findPersonalChatRoomList(Long memberId) {
        return chatRoomRepository.findPersonalRoomByMemberId(memberId);
    }

    //단체 톡방 조회
    @Transactional
    public List<ChatRoom> findGroupChatRoomList(Long memberId) {
        return chatRoomRepository.findGroupRoomByMemberId(memberId);
    }

    //roomId로 채팅방 조회
    @Transactional
    public ChatRoom findChatRoomById(String roomId) {
        return chatRoomRepository.findChatRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방을 찾을 수 없습니다."));
    }

    //ChatRoom 정보 업데이트
    @Transactional
    public void updateChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }

    //소켓 연결 해제 시 시간 정보 업데이트
    @Transactional
    public void updateChatRoomExitTime(Long memberId, String roomId) {
        ChatRoom chatRoom = findChatRoomById(roomId);
        chatRoom.getExitList().put(memberId, LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void deleteChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
    }
}