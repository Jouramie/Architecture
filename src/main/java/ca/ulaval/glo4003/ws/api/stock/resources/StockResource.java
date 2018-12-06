package ca.ulaval.glo4003.ws.api.stock.resources;

import ca.ulaval.glo4003.service.stock.StockDto;
import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.ws.api.stock.assemblers.ApiStockAssembler;
import ca.ulaval.glo4003.ws.api.stock.dtos.ApiStockDto;
import ca.ulaval.glo4003.ws.http.pagination.PaginationBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class StockResource implements DocumentedStockResource {

  private final StockService stockService;
  private final ApiStockAssembler apiStockAssembler;

  @Inject
  public StockResource(StockService stockService, ApiStockAssembler apiStockAssembler) {
    this.stockService = stockService;
    this.apiStockAssembler = apiStockAssembler;
  }

  @GET
  @Path("/{title}")
  @Override
  public ApiStockDto getStockByTitle(@PathParam("title") String title) {
    StockDto stockDto = stockService.getStockByTitle(title);
    return apiStockAssembler.toDto(stockDto);
  }

  @GET
  @PaginationBinding
  @Override
  public List<ApiStockDto> getStocks(@QueryParam("name") String name, @QueryParam("category") String category,
                                     @QueryParam("page") int page, @QueryParam("per_page") int perPage) {
    List<StockDto> stockDtos = stockService.queryStocks(name, category);
    return apiStockAssembler.toDtoList(stockDtos);
  }
}
