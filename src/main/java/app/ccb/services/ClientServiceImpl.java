package app.ccb.services;

import app.ccb.domain.dtos.ClientImportDto;
import app.ccb.domain.entities.Client;
import app.ccb.domain.entities.Employee;
import app.ccb.repositories.ClientRepository;
import app.ccb.repositories.EmployeeRepository;
import app.ccb.util.Constants;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, EmployeeRepository employeeRepository, Gson gson, FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public Boolean clientsAreImported() {
        
       return this.clientRepository.count() != 0;
    }

    @Override
    public String readClientsJsonFile() throws IOException {
        return this.fileUtil.readFile(Constants.CLIENTS_FILE_PATH);
    }

    @Override
    public String importClients(String clients) {
        StringBuilder importClientResult = new StringBuilder();

        ClientImportDto[] clientImportDtos = this.gson.fromJson(clients, ClientImportDto[].class);

        for (ClientImportDto clientImportDto : clientImportDtos) {
            if(!this.validationUtil.isValid(clientImportDto)){
                importClientResult.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;
            }

            String clientFullName = String.format("%s %s", clientImportDto.getFirstName(), clientImportDto.getLastName());
            Client client = (Client) this.clientRepository.findByFullName(clientFullName).orElse(null);
            
            if(client != null){
                importClientResult.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;
            }
            
            client = this.modelMapper.map(clientImportDto, Client.class);
            
            String firstNameEmployee = clientImportDto.getAppointedEmployee().split("\\s+")[0];
            String lastNameEmployee = clientImportDto.getAppointedEmployee().split("\\s+")[1];

            Employee employee = this.employeeRepository.
                    findByFirstNameAndLastName(firstNameEmployee, lastNameEmployee).orElse(null);

            if(employee == null){
                importClientResult.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;
            }
            
            client.setFullName(clientFullName);
            client.getEmployees().add(employee);
            
            importClientResult.append(String.format("Successfully imported Client - %s", clientFullName))
                    .append(System.lineSeparator()) ;
            
            this.clientRepository.saveAndFlush(client);
        }
        
        return importClientResult.toString().trim();
    }

    @Override
    public String exportFamilyGuy() {
        // TODO : Implement Me
        return null;
    }
}
