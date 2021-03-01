package iths.tlj.lab2i.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

//info at: https://www.baeldung.com/rest-api-search-language-spring-data-specifications
//KEY: the field name, eg firstName, nationality etc
//OPERATION: eg > or <= etc
//VALUE: "Jimi", "American" etc
@Data
@NoArgsConstructor
public class SearchCriteria {
private String key;
private String operation;
private Object value;

    public SearchCriteria(String key, String operation, Object value) {
    this.key = key;
    this.operation = operation;
    this.value = value;
    }
}
