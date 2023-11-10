package fc.server.palette.secondhand.controller;

import fc.server.palette._common.exception.Exception403;
import fc.server.palette._common.s3.S3DirectoryNames;
import fc.server.palette._common.s3.S3ImageUploader;
import fc.server.palette.member.auth.CustomUserDetails;
import fc.server.palette.purchase.dto.request.RemoveImageDto;
import fc.server.palette.secondhand.dto.request.CreateProductDto;
import fc.server.palette.secondhand.dto.request.EditProductDto;
import fc.server.palette.secondhand.dto.response.ProductDto;
import fc.server.palette.secondhand.dto.response.ProductListDto;
import fc.server.palette.secondhand.entity.Bookmark;
import fc.server.palette.secondhand.entity.Media;
import fc.server.palette.secondhand.entity.Secondhand;
import fc.server.palette.secondhand.service.SecondhandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static fc.server.palette._common.exception.message.ExceptionMessage.CANNOT_BOOKMARK_YOURS;

@RestController
@RequestMapping("/api/secondhand")
@RequiredArgsConstructor
public class SecondhandController {
    private final SecondhandService secondhandService;
    private final S3ImageUploader s3ImageUploader;

    @GetMapping("")
    public ResponseEntity<List<ProductListDto>> getAllProducts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProductListDto> products = secondhandService.getAllProducts(userDetails.getMember().getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProductDto product = secondhandService.getProduct(productId, userDetails.getMember().getId());
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(secondhandService.getAuthorId(productId));
        secondhandService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{productId}/closing")
    public ResponseEntity<ProductDto> closeTransaction(@PathVariable Long productId,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(secondhandService.getAuthorId(productId));
        ProductDto product = secondhandService.closeTransaction(productId);
        return new ResponseEntity<ProductDto>(product, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ProductDto> createProduct(@RequestPart("dto") CreateProductDto createProductDto,
                                                    @RequestPart("file") List<MultipartFile> images,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        Secondhand product = createProductDto.toEntity(userDetails.getMember());
        List<String> savedImageUrls = s3ImageUploader.save(S3DirectoryNames.SECONDHAND, images);
        List<Media> mediaList = toMediaList(savedImageUrls, product);
        ProductDto savedProduct = secondhandService.createProduct(product, mediaList);
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }

    private List<Media> toMediaList(List<String> images, Secondhand secondhand) {
        return images.stream().map(url -> Media.builder()
                        .secondhand(secondhand)
                        .url(url)
                        .build())
                .collect(Collectors.toList());
    }

    @PatchMapping(value = "/{productId}", params = {"dto", "removeFileUrl"})
    public ResponseEntity<ProductDto> EditProduct(@PathVariable Long productId,
                                                  @RequestPart("dto") EditProductDto editProductDto,
                                                  @RequestPart(value = "file", required = false) List<MultipartFile> images,
                                                  @RequestPart("removeFileUrl") RemoveImageDto removeImageDto,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(secondhandService.getAuthorId(productId));

        saveImages(images, productId);

        s3ImageUploader.remove(removeImageDto.getUrls());
        secondhandService.deleteImages(removeImageDto.getUrls());

        ProductDto product = secondhandService.editProduct(productId, editProductDto);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PatchMapping(value = "/{productId}", params = {"dto"})
    public ResponseEntity<ProductDto> EditProduct(@PathVariable Long productId,
                                                  @RequestPart("dto") EditProductDto editProductDto,
                                                  @RequestPart(value = "file", required = false) List<MultipartFile> images,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.validateAuthority(secondhandService.getAuthorId(productId));

        saveImages(images, productId);

        ProductDto product = secondhandService.editProduct(productId, editProductDto);

        return new ResponseEntity<>(product, HttpStatus.OK);

    }

    @PostMapping("{productId}/bookmark")
    public ResponseEntity<?> addBookmark(@PathVariable Long productId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getMember().getId().equals(secondhandService.getAuthorId(productId))) {
            throw new Exception403(CANNOT_BOOKMARK_YOURS);
        }
        Bookmark bookmark = Bookmark.of(secondhandService.getSecondhand(productId),
                userDetails.getMember());
        secondhandService.addBookmark(productId, userDetails.getMember());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{productId}/unbookmark")
    public ResponseEntity<?> unbookmarkProduct(@PathVariable Long productId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        secondhandService.unbookmarkProduct(productId, userDetails.getMember());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void saveImages(List<MultipartFile> images, Long productId) {
        if (images != null) {
            //s3 저장
            List<String> savedImageUrls = s3ImageUploader.save(S3DirectoryNames.SECONDHAND, images);
            //db 저장
            List<Media> MediaList = toMediaList(savedImageUrls, secondhandService.getSecondhand(productId));
            secondhandService.saveImages(MediaList);
        }
    }
}
