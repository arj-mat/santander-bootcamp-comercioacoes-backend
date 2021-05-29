package com.santander.bootcamp.comercioacoes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppUtils {
    public static String ObjectToJSON(Object value) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer();

        return ow.writeValueAsString( value );
    }

    public static Object MapToDTO(Map<?, ?> sourceMap, Class targetDTOClass) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.convertValue( sourceMap, targetDTOClass );
        } catch ( Exception e ) {
            if ( e.getCause() instanceof MismatchedInputException ) {
                throw new Error(
                        String.format(
                                "AppUtils.MapToDTO: o objeto Map possui uma entrada que não é compatível com o tipo que é esperado pelo " +
                                        "DTO %s: %s",
                                targetDTOClass.getName(),
                                e.getMessage()
                        ) );
            } else {
                throw e;
            }
        }
    }

    /**
     * Transforma um array de pares chave e valor em um Map. Exemplo de array:
     * new Object[][]{ {"chave 1", "valor 1"}, {"chave 2", "valor 2"} }
     *
     * @param arrayOfKeyValue Array de pares chave, valor
     * @return Retorna um novo objeto Map.
     */
    public static <T> Map PairArrayToMap(Object[][] arrayOfKeyValue) {
        return Stream.of( arrayOfKeyValue ).collect( Collectors.toMap(
                data -> {
                    return data[0];
                },
                data -> {
                    return data[1];
                } )
        );
    }

    public static <T> Map ObjectToMap(Object sourceObject) {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.convertValue( sourceObject, Map.class );
    }
}
