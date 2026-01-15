package com.wiktoria.flow.flowsmapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataManagerTest {

    @Test
    void testSingletonPattern() {
        DataManager instance1 = DataManager.getInstance();
        DataManager instance2 = DataManager.getInstance();
        assertSame(instance1, instance2, "To musi być ten sam obiekt (Singleton)");
    }

    @Test
    void testAddingData() {
        DataManager db = DataManager.getInstance();

        String uniqueName = "TestUser" + System.currentTimeMillis();
        User u = new RegularUser("t@t.pl", uniqueName, "1");
        db.addUser(u);

        assertNotNull(db.getUserByUsername(uniqueName));
        assertEquals(uniqueName, db.getUserByUsername(uniqueName).getUsername());
    }
}