package com.example.jackson.homelessshelter.Controller;

import com.example.jackson.homelessshelter.Model.Shelter;
import com.example.jackson.homelessshelter.Model.User;

import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Test
 */

public class FieldsPopulatedTest {

    // Jackson Cook

    @Test
    public void fieldsPopulated() throws Exception {
        LoginFragment loginFragment = new LoginFragment();
        assertEquals(false, loginFragment.fieldsPopulated("", ""));
        assertEquals(false, loginFragment.fieldsPopulated("", "legend"));
        assertEquals(false, loginFragment.fieldsPopulated("legend", ""));
        assertEquals(true, loginFragment.fieldsPopulated("legend", "legend"));
    }

    // Chris Truong

    @Test
    public void matches() throws Exception {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        assertEquals(true, registrationFragment.matches("swag", "swag"));
        assertEquals(false, registrationFragment.matches("swag", "lag"));
    }

    // Josh Philliber

    @Test
    public void isValidEmail() throws Exception {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        assertEquals(true, registrationFragment.isValidEmail("swag@cat.com"));
        assertEquals(false, registrationFragment.isValidEmail("swag@cat.cm"));
        assertEquals(false, registrationFragment.isValidEmail("swag@.com"));
        assertEquals(false, registrationFragment.isValidEmail("swag"));
        assertEquals(false, registrationFragment.isValidEmail("swag@"));
        assertEquals(false, registrationFragment.isValidEmail("swag@."));
    }

    // Tim

    @Test
    public void isBeingFiltered() throws Exception {
        SheltersFragment sheltersFragment = new SheltersFragment();
        assertEquals(false, sheltersFragment
                .isBeingFiltered("Anyone", "Both", ""));
        assertEquals(true, sheltersFragment
                .isBeingFiltered("Anyone", "swag", ""));
        assertEquals(true, sheltersFragment
                .isBeingFiltered("swag", "Both", ""));
        assertEquals(true, sheltersFragment
                .isBeingFiltered("Anyone", "Both", "swag"));
        assertEquals(true, sheltersFragment
                .isBeingFiltered("swag", "Both", "swag"));
        assertEquals(true, sheltersFragment
                .isBeingFiltered("Anyone", "swag", "swag"));
        assertEquals(true, sheltersFragment
                .isBeingFiltered("swag", "swag", "swag"));
        assertEquals(true, sheltersFragment
                .isBeingFiltered("swag", "swag", ""));
    }

    // Johnny

    @Test
    public void canReserveRoom() throws Exception {
        ShelterDetails shelterDetails = new ShelterDetails();
        User user = new User();
        user.setCurrentShelter("NA");
        Shelter shelter = new Shelter();
        shelter.setCapacity(100);
        assertEquals(true, shelterDetails
                .canReserveRoom(55, user, shelter));
        assertEquals(false, shelterDetails
                .canReserveRoom(105, user, shelter));
        user.setCurrentShelter("Swag");
        assertEquals(false, shelterDetails
                .canReserveRoom(55, user, shelter));
    }

//    @Test
//    public void filterAge() throws Exception {
//        SheltersFragment sheltersFragment = new SheltersFragment();
//        assertEquals(new ArrayList<String>(){{
//            add("Hope Atlanta");}},
//                sheltersFragment.filterAge("Veterans"));
//        assertEquals(new ArrayList<String>() {{
//            add("My Sister's House");
//            add("The Atlanta Day Center for Women & Children");
//            add("The Shepherd's Inn");
//            add("Fuqua Hall");
//            add("Atlanta's Children Center");
//            add("Eden Village - Families");
//            add("Eden Village - Singles");
//            add("Our House");
//            add("Covenant House Georgia");
//            add("Nicholas House");
//            add("Hope Atlanta");
//            add("Gateway Center");
//            add("Young Adult Guidance Center");
//            add("Homes of Light");}}, sheltersFragment.filterAge("Anyone"));
//    }

}