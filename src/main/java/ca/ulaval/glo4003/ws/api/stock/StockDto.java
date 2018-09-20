package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "StockResponse",
        description = "Stock response containing title, market, stock name, category, " +
                "stock value at market openning, current stock value and stock value at market close."
)

public class StockDto {
    @Schema(description = "Title")
    public final String title;
    @Schema(description = "Market")
    public final String market;
    @Schema(description = "Name of the company")
    public final String stockName;
    @Schema(description = "Category")
    public final String category;
    @Schema(description = "Stock value at market openning")
    public final double open;
    @Schema(description = "Current stock value")
    public final double current;
    @Schema(description = "Stock value at market close")
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
