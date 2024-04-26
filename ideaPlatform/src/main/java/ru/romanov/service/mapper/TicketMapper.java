package ru.romanov.service.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.romanov.service.dto.TicketDto;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TicketMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final File file;

    public TicketMapper(String jsonFilePath) {
        this.file = new File(jsonFilePath);
    }

    /**
     * Convert tickets.json(json Array) to List of TicketDto (internal entity)
     *
     * @return List TicketDto(pojo)
     */
    public List<TicketDto> jsonToListDto() throws IOException {
        ArrayNode jsonNodes = (ArrayNode) objectMapper.readTree(file).get("tickets");
        ArrayList<TicketDto> tickets = new ArrayList<>();

        if (jsonNodes != null && jsonNodes.isArray())
            for (JsonNode ticketNode : jsonNodes) {
                TicketDto ticketDto = objectMapper.convertValue(ticketNode, TicketDto.class);
                tickets.add(ticketDto);
            }
        return tickets;
    }
}
