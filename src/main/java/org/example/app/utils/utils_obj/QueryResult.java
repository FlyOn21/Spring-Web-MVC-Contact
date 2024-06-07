package org.example.app.utils.utils_obj;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.domain.entity.base_entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QueryResult<T extends BaseEntity> {
    private boolean isSuccess = false;
    private List<String> errors = new ArrayList<>();
    private List<T> entity = new ArrayList<>();
    private String msg;

    public void addError(String error) {
        this.errors.add(error);
    }
}
