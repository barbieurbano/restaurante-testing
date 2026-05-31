package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Employee;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.enums.WorkLevel;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class EmployeeRepositoryTest {
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    //Agregamos EntityManager para poder limpiea la memoria y forzar consultas a base de datos
    @Autowired
    EntityManager entityManager;

    //Declarar datos para el TEST
    Employee empleado1;
    Employee empleado2;

    @BeforeEach // Se ejecuta antes de cada TEST
    void setUp(){
        System.out.println("Ejecutando before each");
        //Inicializar DATOS para el test
        empleado1 = new Employee();
        empleado1.setNif("M18");
        empleado1.setActive(true);
        employeeRepository.save(empleado1);

        empleado2 = Employee.builder().nif("B10").active(false).build();
        employeeRepository.save(empleado2);
    }
    @Test
    void count(){
        assertEquals(2, employeeRepository.count());
    }

    @Test
    void existsById(){
        assertTrue(employeeRepository.existsById(empleado1.getId()));
    }

//Al buscar un empleado por su id, lo encuentra y trae sus datos correcto
    @Test
    void findById(){
        Optional<Employee> empleadoOptional = employeeRepository.findById(empleado1.getId());
        assertTrue(empleadoOptional.isPresent()); //confirma que findById encontró algo (el Optional no está vacío).
        Employee empleado = empleadoOptional.get();
        assertEquals("M18", empleado.getNif()); //confirma que el empleado recuperado tiene el NIF esperado.
    }

    @Test
    void findAll(){
        List<Employee> empleados = employeeRepository.findAll();
        assertNotNull(empleados);
        assertTrue(empleados.size() >= 2);
    }

    @Test
    void save(){
        //Usar datos ya cargados
        Employee empleado = new Employee();
        empleado.setNif("P19");
        employeeRepository.save(empleado);
        assertNotNull(empleado.getId()); // Se le asigna automaticamente el id 3
        assertTrue(employeeRepository.existsById(empleado.getId()));
    }

    @Test
    void saveAll(){
        //Opcion clasica, crear una lista mutable (new)
        //Lista de nombres
        String[] nombres = new String[]{"S12", "C18", "T21"};
        List<String> nombresBien = new ArrayList<>();
        nombresBien.add("HORACIO");
        nombresBien.add("IRMA");

        List<Double> dineros = new ArrayList<>();
        dineros.add(5d);
        dineros.add(6d);

        Employee empleado3 = new Employee();
        Employee empleado4 = new Employee();
        List<Employee> empleados = new ArrayList<>();
        empleados.add(empleado3);
        empleados.add(empleado4);
        employeeRepository.saveAll(empleados);

        assertEquals(4, employeeRepository.count());
    }

    //borrar un empleado realmente lo elimina y deja uno menos
    @Test
    void deleteByID(){
        //Comprobar que SI EXISTE el empleado 1: existById
        assertTrue(employeeRepository.existsById(empleado1.getId())); //ANTES de borrar, confirma que el empleado sí existe.
        long numeroEmpleadoAntes = employeeRepository.count();

        //Borrarlo: con deleteById o delete
        employeeRepository.deleteById(empleado1.getId());

        //comporbar que NO EXISTE el empleado 1
        assertFalse(employeeRepository.existsById(empleado1.getId())); //DESPUES de borrar, confirma que ya no existe.
        long numeroEmpleadosDespues = employeeRepository.count();
        assertEquals(numeroEmpleadoAntes - 1, numeroEmpleadosDespues); //CONFIRMA que la cantidad bajó exactamente en 1 (no borró de más ni de menos).
    }

    @Test
    void workLevelEnum(){
        //JUNIOR
        Employee empleado = new Employee();
        empleado.setLevel(WorkLevel.JUNIOR);

        Employee empleadoGuardado =  employeeRepository.save(empleado);
        assertNotNull(empleadoGuardado.getLevel()); // Se comprueba que el level no es null
        assertEquals(WorkLevel.JUNIOR, empleadoGuardado.getLevel()); // Compruebo que el level ES JUNIOR

        //SENIOR por defecto
        Employee empleadoSenior = new Employee();
        Employee seniorGuardado = employeeRepository.save(empleadoSenior);

        //PROBAR quitar lo que sea denior por defecto para ver cómo falla la comparacion
        assertEquals(WorkLevel.SENIOR, seniorGuardado.getLevel());
    }

    @Test
    void startDate(){
        //Probar fecha por DEFECTO startDate
        //Crear objeto EMPLOYEE con new o builder

        //GIVEN
        Employee empleado = new Employee();
        empleado.setNif("F21");

        //WHEN
        Employee empleadoGuardado = employeeRepository.save(empleado);

        //THEN - assert startDate not null
        assertNotNull(empleadoGuardado.getStartDate());
        assertEquals(LocalDate.now(), empleadoGuardado.getStartDate());

        //PROBAR cambniar fecha y ver si funciona
        empleadoGuardado.setStartDate(LocalDate.now().plusDays(30));
        empleadoGuardado = employeeRepository.save(empleadoGuardado);
        assertEquals(LocalDate.now().plusDays(30), empleadoGuardado.getStartDate());

        //Tener en cuenta que el BUILD pone en nullo los atributos que no cargamos.
    }

    @Test
    void manyToOneRestaurantTest(){
        //PASO 1 - Crear un restaurante y guardarlo
        Restaurant restaurante = new Restaurant();
        restaurante.setName("Margaretha");
        restaurantRepository.save(restaurante);

        //PASO 2 - Crear un empleado y asociarle el restaurante
        Employee empleado = new Employee();
        empleado.setNif("G8");
        empleado.setRestaurant(restaurante);
        Employee empleadoGuardado = employeeRepository.save(empleado);

        assertNotNull(empleadoGuardado.getRestaurant());

        //PASO 3 - Limpiar la memoria para forzar la consulta de findByID a BD
        entityManager.flush(); // Sincronizar cambios pendientes con la BD
        entityManager.clear(); // Limpiar la memoria

        //Buscar el empleado de la BD para ver si viene con el restaurante asociado
        Employee empleadoDB = employeeRepository.findById(empleado.getId()).get();
        assertNotNull(empleadoDB.getRestaurant());
        System.out.println(empleadoDB);
        System.out.println(empleadoDB.getRestaurant());
    }

    //Filtros BASICOS
    //FILTRAR empleados por nivel JUNIOR
    @Test
    void findByLevel(){
        employeeRepository.deleteAll();
        employeeRepository.save(Employee.builder().nif("1").level(WorkLevel.JUNIOR).build());
        employeeRepository.save(Employee.builder().nif("2").level(WorkLevel.JUNIOR).build());
        employeeRepository.save(Employee.builder().nif("3").level(WorkLevel.SENIOR).build());
        employeeRepository.save(Employee.builder().nif("4").level(WorkLevel.SENIOR).build());

        //PARA probar un filtro, necesitás datos que NO cumplan la condición. Si los 4 son JUNIOR, el filtro
        // podría estar roto (devolver ALL) y el test no se daría cuenta.
        // Con 2 JUNIOR + 2 SENIOR, el assertEquals(2, ...) prueba de verdad que el filtro separa
        // JUNIOR de SENIOR.
        List<Employee> juniors = employeeRepository.findAllByLevel(WorkLevel.JUNIOR);
        assertEquals(2, juniors.size()); // CONFIRMA que el filtro devolvió exactamente los 2 JUNIOR (y dejó afuera los 2 SENIOR)

        //siempre es mejor que filtre la BD para no traer TODOS los objetos a java

    }

    @Test
    void findAllBy_ActiveTrue_And_RestaurantName(){
        employeeRepository.deleteAll();
        //PASO 1 - Crear dos restaurantes
        Restaurant restaurante1 = new Restaurant();
        restaurante1.setName("Son son");
        restaurantRepository.save(restaurante1);

        Restaurant restaurante2 = restaurantRepository.save(Restaurant.builder().name("Sabor sabor").build());

        //PASO 2 - Crear 4 empleado, dos rtestaurante
        Employee empleado1 = new Employee();
        empleado1.setNif("1");
        empleado1.setActive(true);
        empleado1.setRestaurant(restaurante1);
        employeeRepository.save(empleado1);

        Employee empleado2 =  employeeRepository.save(Employee.builder().nif("2").active(true).restaurant(restaurante1).build());
        Employee empleado3 =  employeeRepository.save(Employee.builder().nif("3").active(true).restaurant(restaurante2).build());
        Employee empleado4 =  employeeRepository.save(Employee.builder().nif("4").active(false).restaurant(restaurante2).build());

        //PASO 3 - Invocar el nuevo meto findAllBy..
        List<Employee> empleadoActivosDelSaborSabor = employeeRepository.findAllByActiveTrueAndRestaurantName("Sabor sabor");

        //PASO 4 - assert
        assertEquals(1, empleadoActivosDelSaborSabor.size());
    }

    @Test
    void antiguedadEnDiasTest(){
        Employee empleado = new Employee();
        empleado.setNif("M23");
        empleado.setStartDate(LocalDate.of(2023, 2, 13 ));
        employeeRepository.save(empleado);

        Duration days = employeeRepository.findWorkDaysByNif("M23");
        System.out.println(days.toDays());

        long antiguedadEnDias = ChronoUnit.DAYS.between(empleado.getStartDate(),LocalDate.now());
        System.out.println(antiguedadEnDias);
    }
}