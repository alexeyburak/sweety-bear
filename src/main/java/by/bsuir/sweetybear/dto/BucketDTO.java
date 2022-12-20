package by.bsuir.sweetybear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BucketDTO {

    private int amountProducts;
    private Double sum;
    private List<BucketDetailDTO> bucketDetails = new ArrayList<>();

    public void aggregate() {
        this.amountProducts = bucketDetails.size();
        this.sum = bucketDetails.stream()
                .map(BucketDetailDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public boolean isBucketEmpty() {
        return bucketDetails.size() == 0;
    }

}
