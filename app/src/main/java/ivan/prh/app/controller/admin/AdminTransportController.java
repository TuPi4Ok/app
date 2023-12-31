package ivan.prh.app.controller.admin;

import io.swagger.annotations.*;
import ivan.prh.app.controller.BaseController;
import ivan.prh.app.dto.transport.AdminTransportDto;
import ivan.prh.app.model.Rent;
import ivan.prh.app.model.Transport;
import ivan.prh.app.service.admin.AdminTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/Admin/Transport")
@Api(value = "AdminTransportController", description = "Админ-контроллер для управления транспортом")
public class AdminTransportController extends BaseController {

    @Autowired
    AdminTransportService transportService;

    @ApiOperation(value = "Получение списка всех транспортов", response = Rent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Неправильный логин или пароль"),
            @ApiResponse(code = 403, message = "Недостаточно прав"),
            @ApiResponse(code = 404, message = "Транспорт не найден")
    })
    @GetMapping("")
    public List<Transport> getTransports(
            @ApiParam(value = "Начало выборки", required = true) @RequestParam("start") int start,
            @ApiParam(value = "Размер выборки", required = true) @RequestParam("count") int count) {
        return transportService.getTransport(start, count);
    }
    @ApiOperation(value = "Получение транспорта по Id", response = Rent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Неправильный логин или пароль"),
            @ApiResponse(code = 403, message = "Недостаточно прав"),
            @ApiResponse(code = 404, message = "Транспорт не найден")
    })
    @GetMapping("/{id}")
    public Transport getTransport(
            @ApiParam(value = "Id транспорта", required = true) @PathVariable("id") long id) {
        return transportService.getTransport(id);
    }

    @ApiOperation(value = "Создание транспорта", response = Rent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Неправильный логин или пароль"),
            @ApiResponse(code = 403, message = "Недостаточно прав"),
            @ApiResponse(code = 404, message = "Транспорт не найден")
    })
    @PostMapping("")
    public Transport createTransport(@Valid @RequestBody AdminTransportDto transportDto) {
        return transportService.createTransport(transportDto);
    }
    @ApiOperation(value = "Обновление транспорта по Id", response = Rent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Неправильный логин или пароль"),
            @ApiResponse(code = 403, message = "Недостаточно прав"),
            @ApiResponse(code = 404, message = "Транспорт не найден")
    })
    @PutMapping("/{id}")
    public Transport updateTransport(
            @ApiParam(value = "Id транспорта", required = true) @PathVariable("id") long id,
            @Valid @RequestBody AdminTransportDto transportDto) {
        return transportService.updateTransport(id, transportDto);
    }

    @ApiOperation(value = "Удаление транспорта по Id", response = Rent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Неправильный логин или пароль"),
            @ApiResponse(code = 403, message = "Недостаточно прав"),
            @ApiResponse(code = 404, message = "Транспорт не найден")
    })
    @DeleteMapping("/{id}")
    public void deleteTransport(@ApiParam(value = "Id транспорта", required = true)@PathVariable("id") long id) {
        transportService.deleteTransport(id);
    }

}
