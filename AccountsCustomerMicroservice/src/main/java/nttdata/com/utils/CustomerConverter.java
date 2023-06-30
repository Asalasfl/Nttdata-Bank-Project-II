package nttdata.com.utils;

import nttdata.com.dto.CustomerDTO;
import nttdata.com.model.Customer;

public class    CustomerConverter {

    // Metodo que convierte un Customer Entity a un Customer Dto
    public static CustomerDTO customerDTO(Customer entity){
        CustomerDTO dto = new CustomerDTO();
        dto.setIdCustomer(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setAccounts(entity.getAccountIds());
        dto.setCredits(entity.getCreditIds());
        dto.setCreditCards(entity.getCreditCardIds());

        return dto;
    }

    // Metodo que convierte un Alumno Dto a un Alumno Entity
    public static Alumno alumnoToEntity(AlumnoDto dto){
        Alumno entity = new Alumno();
        entity.setIdAlumno(dto.getStudentId());
        entity.setDni(dto.getDni());
        entity.setNombre(dto.getName());
        entity.setApellido(dto.getLastName());
        entity.setFechaNacimiento(dto.getBirthdate());
        entity.setEstado(dto.getState());

        return entity;
    }
}