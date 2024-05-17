package org.iesvdm.appointment.repository;

import org.iesvdm.appointment.entity.Appointment;
import org.iesvdm.appointment.entity.AppointmentStatus;
import org.iesvdm.appointment.entity.Customer;
import org.iesvdm.appointment.entity.User;
import org.iesvdm.appointment.repository.impl.AppointmentRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class AppointmentRepositoryImplTest {

    private Set<Appointment> appointments;

    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setup() {
        appointments = new HashSet<>();
        appointmentRepository = new AppointmentRepositoryImpl(appointments);
    }

    /**
     * Crea 2 citas (Appointment) una con id 1 y otra con id 2,
     * resto de valores inventados.
     * Agrégalas a las citas (appointments) con la que
     * construyes el objeto appointmentRepository bajo test.
     * Comprueba que cuando invocas appointmentRepository.getOne con uno
     * de los id's anteriores recuperas obtienes el objeto.
     * Pero si lo invocas con otro id diferente recuperas null
     */
    @Test
    void getOneTest() {
        Appointment appointment = new Appointment();
        appointment.setId(1);
        Appointment appointment1 = new Appointment();
        appointment1.setId(2);
        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment1);
        assertThat(appointmentRepository.getOne(1)).isEqualTo(appointment);
        assertThat(appointmentRepository.getOne(3)).isNull();
    }

    /**
     * Crea 2 citas (Appointment) y guárdalas mediante
     * appointmentRepository.save.
     * Comprueba que la colección appointments
     * contiene sólo esas 2 citas.
     */
    @Test
    void saveTest() {
        Appointment appointment = new Appointment();
        appointment.setId(1);
        Appointment appointment1 = new Appointment();
        appointment1.setId(2);
        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment1);
        assertThat(appointments.size()).isEqualTo(2);
    }

    /**
     * Crea 2 citas (Appointment) una cancelada por un usuario y otra no,
     * (atención al estado de la cita, lee el código) y agrégalas mediante
     * appointmentRepository.save a la colección de appointments
     * Comprueba que mediante appointmentRepository.findCanceledByUser
     * obtienes la cita cancelada.
     */
    @Test
    void findCanceledByUserTest() {
        Appointment appointment = new Appointment();
        User user = new User();
        user.setId(1);
        appointment.setId(1);
        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.setCanceler(user);

        Appointment appointment1 = new Appointment();
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setId(2);
        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment1);
        //Cuidado con tener un status null
        assertThat(appointmentRepository.findCanceledByUser(1)).contains(appointment);
    }

    /**
     * Crea 3 citas (Appointment), 2 para un mismo cliente (Customer)
     * con sólo una cita de ellas presentando fecha de inicio (start)
     * y fin (end) dentro del periodo de búsqueda (startPeriod,endPeriod).
     * Guárdalas mediante appointmentRepository.save.
     * Comprueba que appointmentRepository.findByCustomerIdWithStartInPeroid
     * encuentra la cita en cuestión.
     * Nota: utiliza LocalDateTime.of(...) para crear los LocalDateTime
     */
    @Test
    void findByCustomerIdWithStartInPeroidTest() {
        Appointment appointment = new Appointment();
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();
        Customer customer = new Customer();
        customer.setId(1);
        appointment1.setCustomer(customer);
        appointment2.setCustomer(customer);
        LocalDateTime inicio = LocalDateTime.of(2024,10,10,10,10,10);
        LocalDateTime fin = LocalDateTime.of(2025,10,10,10,10,10);
        appointment1.setStart(inicio);
        appointment1.setEnd(fin);
        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        //El tamaño de la lista te devuelve 0 pero deberia tener algun elemento
        assertThat(appointmentRepository.findByCustomerIdWithStartInPeroid(1,inicio,fin).size()).isEqualTo(1);
    }


    /**
     * Crea 2 citas (Appointment) una planificada (SCHEDULED) con tiempo fin
     * anterior a la tiempo buscado por appointmentRepository.findScheduledWithEndBeforeDate
     * guardándolas mediante appointmentRepository.save para la prueba de findScheduledWithEndBeforeDate
     *
     */
    @Test
    void findScheduledWithEndBeforeDateTest() {
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setStatus(AppointmentStatus.CONFIRMED);
        LocalDateTime fin = LocalDateTime.of(2025,10,10,10,10,10);
        appointment1.setEnd(fin);
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        assertThat(appointmentRepository.findScheduledWithEndBeforeDate(fin).size()).isEqualTo(1);
        //El tamaño de la lista te devuelve 0 pero deberia tener algun elemento
    }


    /**
     * Crea 3 citas (Appointment) planificadas (SCHEDULED)
     * , 2 para un mismo cliente, con una elegible para cambio (con fecha de inicio, start, adecuada)
     * y otra no.
     * La tercera ha de ser de otro cliente.
     * Guárdalas mediante appointmentRepository.save
     * Comprueba que getEligibleAppointmentsForExchange encuentra la correcta.
     */
    @Test
    void getEligibleAppointmentsForExchangeTest() {
        Appointment appointment = new Appointment();
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setStatus(AppointmentStatus.SCHEDULED);
        Customer customer = new Customer();
        Customer customer1 = new Customer();
        LocalDateTime inicio = LocalDateTime.of(2024,10,10,10,10,10);
        LocalDateTime fin = LocalDateTime.of(2025,10,10,10,10,10);
        appointment1.setStart(inicio);
        appointment1.setEnd(fin);
        appointment.setStart(inicio);
        appointment.setEnd(fin);
        appointment2.setStart(inicio);
        appointment2.setEnd(fin);
        customer1.setId(0);
        customer.setId(1);
        appointment1.setCustomer(customer);
        appointment2.setCustomer(customer);
        appointment.setCustomer(customer1);
        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        assertThat(appointmentRepository.getEligibleAppointmentsForExchange(inicio,1).size()).isEqualTo(1);
        //El tamaño de la lista te devuelve 0 pero deberia tener algun elemento
    }


    /**
     * Igual que antes, pero ahora las 3 citas tienen que tener
     * clientes diferentes y 2 de ellas con fecha de inicio (start)
     * antes de la especificada en el método de búsqueda para
     * findExchangeRequestedWithStartBefore
     */
    @Test
    void findExchangeRequestedWithStartBeforeTest() {
        Appointment appointment = new Appointment();
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setStatus(AppointmentStatus.SCHEDULED);
        Customer customer = new Customer();
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        LocalDateTime inicio = LocalDateTime.of(2024,10,10,10,10,10);
        LocalDateTime inicioAntes = LocalDateTime.of(2023,10,10,10,10,10);
        LocalDateTime fin = LocalDateTime.of(2025,10,10,10,10,10);
        appointment1.setStart(inicio);
        appointment1.setEnd(fin);
        appointment.setStart(inicioAntes);
        appointment.setEnd(fin);
        appointment2.setStart(inicioAntes);
        appointment2.setEnd(fin);
        customer1.setId(0);
        customer.setId(1);
        customer2.setId(2);
        appointment1.setCustomer(customer);
        appointment2.setCustomer(customer1);
        appointment.setCustomer(customer2);
        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        //El tamaño de la lista te devuelve 0 pero deberia tener algun elemento
        assertThat(appointmentRepository.getEligibleAppointmentsForExchange(inicio,1).size()).isEqualTo(1);
    }
}
