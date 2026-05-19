package kr.hs.dgsw.esm.domain.product.service;

import kr.hs.dgsw.esm.domain.product.dto.response.ProductResponse;
import kr.hs.dgsw.esm.domain.product.repository.ProductRepository;
import kr.hs.dgsw.esm.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponse::of);
    }

    public ProductResponse getProduct(Long id) {
        return productRepository.findById(id)
                .map(ProductResponse::of)
                .orElseThrow(() -> new BusinessException(404, "상품을 찾을 수 없습니다.", "PRODUCT_NOT_FOUND"));
    }
}
