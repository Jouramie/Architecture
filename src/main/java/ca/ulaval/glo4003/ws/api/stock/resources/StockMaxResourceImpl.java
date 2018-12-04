package ca.ulaval.glo4003.ws.api.stock.resources;

import ca.ulaval.glo4003.service.stock.max.StockMaxValueService;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import ca.ulaval.glo4003.ws.api.stock.assemblers.StockMaxResponseDtoAssembler;
import ca.ulaval.glo4003.ws.api.stock.dtos.StockMaxResponseDto;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/stocks/{title}/max")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Resource
public class StockMaxResourceImpl implements DocumentedStockMaxResource {

  private final StockMaxValueService stockMaxValueService;
  private final StockMaxResponseDtoAssembler assembler;

  @Inject
  public StockMaxResourceImpl(StockMaxValueService stockMaxValueService, StockMaxResponseDtoAssembler assembler) {
    this.stockMaxValueService = stockMaxValueService;
    this.assembler = assembler;
  }

  @GET
  @Override
  public StockMaxResponseDto getStockMaxValue(@PathParam("title") String title) {
    StockMaxValueSummary maxValueSummary = stockMaxValueService.getStockMaxValue(title);
    return assembler.toDto(title, maxValueSummary);
  }
}
