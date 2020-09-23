package DecOfCardsHelper;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DoCContext {

    private String deck_id;
    private String deckCount;
    private Boolean successFlag;
    private String errorDescription;
    private Integer cardsAfterDrawing;
    @Builder.Default
    private Boolean serverErrorFlag=false;
    private Integer remainingCards;
    private Integer expectedRemaining;
    private Integer cardsCount;
    private String pileName;
    private List<String> drownCards;

}
