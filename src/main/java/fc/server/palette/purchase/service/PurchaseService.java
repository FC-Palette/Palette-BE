package fc.server.palette.purchase.service;

import fc.server.palette._common.exception.Exception400;
import fc.server.palette._common.exception.Exception404;
import fc.server.palette._common.exception.message.ExceptionMessage;
import fc.server.palette.member.entity.Member;
import fc.server.palette.purchase.dto.request.EditOfferDto;
import fc.server.palette.purchase.dto.response.MemberDto;
import fc.server.palette.purchase.dto.response.OfferDto;
import fc.server.palette.purchase.dto.response.OfferListDto;
import fc.server.palette.purchase.entity.*;
import fc.server.palette.purchase.entity.type.Category;
import fc.server.palette.purchase.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fc.server.palette._common.exception.message.ExceptionMessage.BOOKMARK_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMediaRepository purchaseMediaRepository;
    private final PurchaseBookmarkRepository purchaseBookmarkRepository;
    private final PurchaseParticipantRepository purchaseParticipantRepository;
    private final PurchaseParticipantMemberRepository purchaseParticipantMemberRepository;

    @Transactional(readOnly = true)
    public List<OfferListDto> getAllOffers(Long memberId) {
        List<Purchase> purchases = purchaseRepository.findAll();
        return purchases.stream()
                .map(purchase -> buildOfferList(purchase, isBookmarked(purchase.getId(), memberId)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OfferDto getOffer(Long offerId) {
        Purchase purchase = purchaseRepository.findById(offerId)
                .orElseThrow(() -> new Exception404(ExceptionMessage.OBJECT_NOT_FOUND));
        return buildOffer(purchase);
    }

    @Transactional
    public OfferDto getOffer(Long offerId, Long loginMember) {
        if (!loginMember.equals(getAuthorId(offerId))) {
            increaseHit(offerId);
        }
        Purchase purchase = purchaseRepository.findById(offerId)
                .orElseThrow(() -> new Exception404(ExceptionMessage.OBJECT_NOT_FOUND));
        return buildOffer(purchase, offerId, loginMember);
    }

    @Transactional(readOnly = true)
    public Purchase getPurchase(Long offerId) {
        return purchaseRepository.findById(offerId)
                .orElseThrow(() -> new Exception404(ExceptionMessage.OBJECT_NOT_FOUND));
    }

    @Transactional
    public void saveImages(List<Media> mediaList) {
        purchaseMediaRepository.saveAll(mediaList);
    }

    @Transactional
    public void deleteImages(List<String> urls) {
        urls
                .forEach(url ->
                        purchaseMediaRepository.delete(
                                purchaseMediaRepository.findByUrl(url)
                        ));
    }

    @Transactional
    public OfferDto createOffer(Purchase purchase, Member member, List<Media> mediaList) {
        Purchase savedPurchase = purchaseRepository.save(purchase);
        participateOffer(purchase.getId(), member);
        purchaseMediaRepository.saveAll(mediaList);
        return buildOffer(savedPurchase);
    }

    private OfferDto buildOffer(Purchase purchase) {
        return OfferDto.builder()
                .id(purchase.getId())
                .member(MemberDto.of(purchase.getMember()))
                .title(purchase.getTitle())
                .category(purchase.getCategory())
                .startDate(purchase.getStartDate())
                .endDate(purchase.getEndDate())
                .price(purchase.getPrice())
                .description(purchase.getDescription())
                .shopUrl(purchase.getShopUrl())
                .headCount(purchase.getHeadCount())
                .bookmarkCount(getBookmarkCount(purchase.getId()))
                .image(getImagesUrl(purchase.getId()))
                .currentParticipantCount(getCurrentParticipants(purchase.getId()))
                .isClosing(purchase.getIsClosing())
                .hits(purchase.getHits())
                .created_at(purchase.getCreatedAt())
                .build();
    }

    private OfferDto buildOffer(Purchase purchase, Long offerId, Long memberId) {
        return OfferDto.builder()
                .id(purchase.getId())
                .member(MemberDto.of(purchase.getMember()))
                .title(purchase.getTitle())
                .category(purchase.getCategory())
                .startDate(purchase.getStartDate())
                .endDate(purchase.getEndDate())
                .price(purchase.getPrice())
                .description(purchase.getDescription())
                .shopUrl(purchase.getShopUrl())
                .headCount(purchase.getHeadCount())
                .bookmarkCount(getBookmarkCount(purchase.getId()))
                .image(getImagesUrl(purchase.getId()))
                .currentParticipantCount(getCurrentParticipants(purchase.getId()))
                .isClosing(purchase.getIsClosing())
                .hits(purchase.getHits())
                .created_at(purchase.getCreatedAt())
                .isParticipating(isParticipating(offerId, memberId))
                .isBookmarked(isBookmarked(offerId, memberId))
                .build();
    }

    private OfferListDto buildOfferList(Purchase purchase, Boolean isBookmarked) {
        return OfferListDto.builder()
                .id(purchase.getId())
                .title(purchase.getTitle())
                .category(purchase.getCategory())
                .price(purchase.getPrice())
                .thumbnailUrl(getThumbnailUrl(purchase.getId()))
                .bookmarkCount(getBookmarkCount(purchase.getId()))
                .hits(purchase.getHits())
                .isBookmarked(isBookmarked)
                .isClosing(purchase.getIsClosing())
                .endDate(purchase.getEndDate())
                .created_at(purchase.getCreatedAt())
                .build();
    }

    private String getThumbnailUrl(Long purchaseId) {
        Optional<Media> optionalThumbnail = purchaseMediaRepository.findAllByPurchase_id(purchaseId)
                .stream()
                .findFirst();
        if (optionalThumbnail.isPresent()) {
            return optionalThumbnail.get().getUrl();
        }
        return ExceptionMessage.OBJECT_NOT_FOUND;
    }

    private List<String> getImagesUrl(Long purchaseId) {
        return purchaseMediaRepository.findAllByPurchase_id(purchaseId)
                .stream()
                .map(Media::getUrl)
                .collect(Collectors.toList());
    }

    private Integer getBookmarkCount(Long purchaseId) {
        return purchaseBookmarkRepository.findAllByPurchase_id(purchaseId).size();
    }

    private Integer getCurrentParticipants(Long purchaseId) {
        List<PurchaseParticipant> allByPurchaseId = purchaseParticipantRepository.findAllByPurchaseId(purchaseId);
        return allByPurchaseId.size();
    }

    @Transactional
    public void addBookmark(Long offerId, Member member) {
        if (isBookmarked(offerId, member.getId())) {
            throw new Exception400(offerId.toString(), BOOKMARK_ALREADY_EXIST);
        }
        purchaseBookmarkRepository.save(Bookmark.of(getPurchase(offerId), member));
    }

    private boolean isBookmarked(Long offerId, Long memberId) {
        Bookmark bookmark = purchaseBookmarkRepository.findByMemberIdAndPurchaseId(memberId, offerId)
                .orElse(null);
        return bookmark != null;
    }

    @Transactional
    public void deleteOffer(Long offerId) {
        purchaseRepository.deleteById(offerId);
    }

    @Transactional
    public OfferDto editOffer(Long offerId, EditOfferDto editOfferDto) {
        Purchase purchase = purchaseRepository.findById(offerId)
                .orElseThrow(() -> new Exception404(ExceptionMessage.OBJECT_NOT_FOUND));
        purchase.updateOffer(editOfferDto);
        return buildOffer(purchase);
    }

    @Transactional
    public OfferDto closeOffer(Long offerId) {
        Purchase purchase = purchaseRepository.findById(offerId)
                .orElseThrow(() -> new Exception404(ExceptionMessage.OBJECT_NOT_FOUND));
        purchase.closeOffer();
        return buildOffer(purchase);
    }

    @Transactional
    public void participateOffer(Long offerId, Member member) {
        if (isParticipating(offerId, member.getId())) {
            throw new Exception400(offerId.toString(), ExceptionMessage.PARTICIPANT_ALREADY_EXIST);
        }
        PurchaseParticipant purchaseParticipant = PurchaseParticipant.of(getPurchase(offerId));
        purchaseParticipantRepository.save(purchaseParticipant);
        ParticipantMember participantMember = ParticipantMember.of(member, purchaseParticipant);
        purchaseParticipantMemberRepository.save(participantMember);
    }

    @Transactional
    public void unbookmarkOffer(Long offerId, Member member) {
        Bookmark bookmark = purchaseBookmarkRepository.findByMemberIdAndPurchaseId(member.getId(), offerId)
                .orElse(null);
        if (bookmark == null) {
            throw new Exception400(offerId.toString(), ExceptionMessage.BOOKMARK_NOT_FOUND);
        }
        purchaseBookmarkRepository.delete(bookmark);
    }

    private boolean isParticipating(Long offerId, Long memberId) {
        List<ParticipantMember> participants = purchaseParticipantMemberRepository.findAllByMemberId(memberId);
        ParticipantMember participantMember = participants
                .stream()
                .filter(participant -> participant
                        .getPurchaseParticipant()
                        .getPurchase()
                        .getId().
                        equals(offerId))
                .findAny()
                .orElse(null);
        return participantMember != null;
    }

    private void increaseHit(Long offerId) {
        Purchase purchase = purchaseRepository.findById(offerId)
                .orElseThrow(() -> new Exception404(ExceptionMessage.OBJECT_NOT_FOUND));
        purchase.increaseHit();
    }

    @Transactional(readOnly = true)
    public Long getAuthorId(Long offerId) {
        Purchase purchase = purchaseRepository.findById(offerId)
                .orElseThrow(() -> new Exception404(ExceptionMessage.OBJECT_NOT_FOUND));
        return purchase.getMember().getId();
    }

    @Transactional
    public int editClosingStatus(LocalDate now) {
        return purchaseRepository.bulkUpdateClosing(now);
    }

    @Transactional(readOnly = true)
    public List<OfferListDto> getFilteredOffers(Category category, int minPrice, int maxPrice, Long memberId) {
        List<Purchase> purchases = purchaseRepository.findAllByCategoryAndPriceBetween(category, minPrice, maxPrice);
        return purchases.stream()
                .map(purchase -> buildOfferList(purchase, isBookmarked(purchase.getId(), memberId)))
                .collect(Collectors.toList());
    }
}
