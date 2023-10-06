package fc.server.palette.purchase.controller;

import fc.server.palette._common.exception.Exception403;
import fc.server.palette._common.s3.S3DirectoryNames;
import fc.server.palette._common.s3.S3ImageUploader;
import fc.server.palette.chat.entity.type.ChatRoomType;
import fc.server.palette.chat.service.ChatRoomService;
import fc.server.palette.member.auth.CustomUserDetails;
import fc.server.palette.purchase.dto.request.EditOfferDto;
import fc.server.palette.purchase.dto.request.GroupPurchaseOfferDto;
import fc.server.palette.purchase.dto.request.RemoveImageDto;
import fc.server.palette.purchase.dto.response.OfferDto;
import fc.server.palette.purchase.dto.response.OfferListDto;
import fc.server.palette.purchase.entity.Media;
import fc.server.palette.purchase.entity.Purchase;
import fc.server.palette.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static fc.server.palette._common.exception.message.ExceptionMessage.CANNOT_BOOKMARK_YOURS;

@RestController
@RequestMapping("/api/groupPurchase")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final S3ImageUploader s3ImageUploader;
    private final ChatRoomService chatRoomService;

    @GetMapping("")
    public ResponseEntity<List<OfferListDto>> getAllOffers(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<OfferListDto> offers = purchaseService.getAllOffers(customUserDetails.getMember().getId());
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<OfferDto> getOffer(@PathVariable Long offerId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        OfferDto offer = purchaseService.getOffer(offerId, userDetails.getMember().getId());
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<OfferDto> createOffer(@RequestPart("dto") GroupPurchaseOfferDto groupPurchaseOfferDto,
                                                @RequestPart("file") List<MultipartFile> images,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Purchase purchase = groupPurchaseOfferDto.toEntity(userDetails.getMember());
        List<String> savedImageUrls = s3ImageUploader.save(S3DirectoryNames.PURCHASE, images);
        List<Media> mediaList = toMediaList(savedImageUrls, purchase);
        OfferDto offer = purchaseService.createOffer(purchase, userDetails.getMember(), mediaList);
        chatRoomService.openGroupChatRoom(offer, userDetails.getMember().getId(), ChatRoomType.PURCHASE);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    private List<Media> toMediaList(List<String> images, Purchase purchase) {
        return images.stream().map(url -> Media.builder()
                        .purchase(purchase)
                        .url(url)
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping("/{offerId}/bookmark")
    public ResponseEntity<?> addBookmark(@PathVariable Long offerId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails.getMember().getId().equals(purchaseService.getAuthorId(offerId))){
            throw new Exception403(CANNOT_BOOKMARK_YOURS);
        }
        purchaseService.addBookmark(offerId, userDetails.getMember());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long offerId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(purchaseService.getAuthorId(offerId));
        purchaseService.deleteOffer(offerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{offerId}", params = {"dto", "removeFileUrl"})
    public ResponseEntity<OfferDto> editOffer(@PathVariable Long offerId,
                                              @RequestPart("dto") EditOfferDto editOfferDto,
                                              @RequestPart(value = "file", required = false) List<MultipartFile> images,
                                              @RequestPart("removeFileUrl") RemoveImageDto removeImageDto,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(purchaseService.getAuthorId(offerId));

        saveImages(images, offerId);

        //s3삭제
        s3ImageUploader.remove(removeImageDto.getUrls());
        //db삭제
        purchaseService.deleteImages(removeImageDto.getUrls());

        //이미지 외 콘텐츠 수정
        OfferDto offer = purchaseService.editOffer(offerId, editOfferDto);

        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PatchMapping(value = "/{offerId}", params = {"dto"})
    public ResponseEntity<OfferDto> editOffer(@PathVariable Long offerId,
                                              @RequestPart("dto") EditOfferDto editOfferDto,
                                              @RequestPart(value = "file", required = false) List<MultipartFile> images,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(purchaseService.getAuthorId(offerId));

        saveImages(images, offerId);

        //이미지 외 콘텐츠 수정
        OfferDto offer = purchaseService.editOffer(offerId, editOfferDto);

        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("/{offerId}/closing")
    public ResponseEntity<OfferDto> closeOffer(@PathVariable Long offerId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(purchaseService.getAuthorId(offerId));
        OfferDto offer = purchaseService.closeOffer(offerId);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping("/{offerId}/participate")
    public ResponseEntity<?> participateOffer(@PathVariable Long offerId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails){
        purchaseService.participateOffer(offerId, userDetails.getMember());
        chatRoomService.participantGroupChatRoom(userDetails.getMember().getId(), offerId, ChatRoomType.PURCHASE);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/{offerId}/unbookmark")
    public ResponseEntity<?> unbookmarkOffer(@PathVariable Long offerId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails){
        purchaseService.unbookmarkOffer(offerId, userDetails.getMember());
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private void saveImages(List<MultipartFile> images, Long offerId) {
        if (images != null) {
            //s3 저장
            List<String> savedImageUrls = s3ImageUploader.save(S3DirectoryNames.PURCHASE, images);
            //db 저장
            List<Media> MediaList = toMediaList(savedImageUrls, purchaseService.getPurchase(offerId));
            purchaseService.saveImages(MediaList);
        }
    }
}
