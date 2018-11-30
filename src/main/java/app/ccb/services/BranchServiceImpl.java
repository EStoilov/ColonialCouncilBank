package app.ccb.services;

import app.ccb.domain.dtos.BranchImportDto;
import app.ccb.domain.entities.Branch;
import app.ccb.repositories.BranchRepository;
import app.ccb.util.Constants;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, FileUtil fileUtil, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.branchRepository = branchRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() != 0;
        
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.fileUtil.readFile(Constants.BRANCHES_FILE_PATH);
    }

    @Override
    public String importBranches(String branchesJson) {
        BranchImportDto[] branchImportDtos = this.gson.fromJson(
                branchesJson, BranchImportDto[].class);

        StringBuilder importResult = new StringBuilder();
        
        for (BranchImportDto branchImportDto : branchImportDtos) {
            if(!this.validationUtil.isValid(branchImportDto)){
                importResult.append("Error: Incorrect Data!")
                        .append(System.lineSeparator());
                
                continue;
            }

            Branch branchEntity = this.modelMapper.map(branchImportDto, Branch.class);

            importResult.append(String.format(
                    "Successfully imported Branch - %s",
                            branchEntity.getName()      
            ))
            .append(System.lineSeparator());
            
            this.branchRepository.saveAndFlush(branchEntity);
        }
        return importResult.toString().trim();
    }
}
