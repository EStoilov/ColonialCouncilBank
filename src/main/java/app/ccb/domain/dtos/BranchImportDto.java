package app.ccb.domain.dtos;

import javax.validation.constraints.NotNull;
public class BranchImportDto {
    private String name;

    public BranchImportDto() {
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
