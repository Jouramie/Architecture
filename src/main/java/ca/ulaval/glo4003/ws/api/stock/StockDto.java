package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "StockResponse",
        description = "Stock response containing stock informations."
)

public class StockDto {
    @Schema(description = "title")
    public final String title;
    @Schema(description = "market")
    public final String market;
    @Schema(description = "name of the company")
    public final String stockName;
    @Schema(description = "category")
    public final String category;
    @Schema(description = "open")
    public final double open;
    @Schema(description = "current")
    public final double current;
    @Schema(description = "close")
    public final double close;

    public StockDto(String title, String market, String stockName, String category,
                    double open, double current, double close) {
        this.title = title;
        this.market = market;
        this.stockName = stockName;
        this.category = category;
        this.open = open;
        this.current = current;
        this.close = close;
    }
}
