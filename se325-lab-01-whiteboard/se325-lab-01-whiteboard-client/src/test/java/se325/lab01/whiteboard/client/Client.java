package se325.lab01.whiteboard.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import se325.lab01.whiteboard.common.Config;
import se325.lab01.whiteboard.common.FullException;
import se325.lab01.whiteboard.common.Graphic;
import se325.lab01.whiteboard.common.Shape;
import se325.lab01.whiteboard.common.ShapeFactory;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test client for the RMI whiteboard application.
 */
public class Client {

    // Proxy object to represent the remote ShapeFactory service.
    private static ShapeFactory proxy;

    /**
     * One-time setup method to retrieve the ShapeFactory proxy from the RMI
     * Registry.
     */
    @BeforeClass
    public static void getProxy() {
        try {
            // Instantiate a proxy object for the RMI Registry, expected to be
            // running on the local machine and on a specified port.
            Registry lookupService = LocateRegistry.getRegistry("localhost", Config.REGISTRY_PORT);

            // Retrieve a proxy object representing the ShapeFactory.
            proxy = (ShapeFactory) lookupService.lookup(Config.SERVICE_NAME);
        } catch (RemoteException e) {
            System.out.println("Unable to connect to the RMI Registry");
        } catch (NotBoundException e) {
            System.out.println("Unable to acquire a proxy for the Concert service");
        }
    }

    /**
     * Test that, using the ShapeFactory proxy, we can invoke methods on the
     * remote ShapeFactory to create remotely accessible Shapes. This test also
     * then invokes methods on the remote Shapes objects, via their acquired
     * proxies.
     */
    @Test
    public void testCreate() throws RemoteException {
        try {
            // Use the ShapeFactory proxy to create a couple of remote Shape
            // instances. newShape() returns proxies for the new remote Shapes.
            Shape shapeA = proxy.newShape(new Graphic(10, 10, 250, 20, true));
            Shape shapeB = proxy.newShape(new Graphic(35, 60, 100, 50, false));

            // Query the new Shape object's ids. the getId() calls are remote
            // method invocations on the Shapes that have been created on the
            // the server.
            System.out.println("ShapeA's Id is " + shapeA.getId());
            System.out.println("ShapeB's Id is " + shapeB.getId());

            // Query the remote factory.
            List<Shape> remoteShapes = proxy.allShapes();

            assertTrue(remoteShapes.contains(shapeA));
            assertTrue(remoteShapes.contains(shapeB));
            assertEquals(2, remoteShapes.size());

            for (Shape s : remoteShapes) {
                // First iteration of this loop calls getAllstate() on the
                // same remote Shape object that shapeA acts as a remote
                // reference for, the second iteration on shapeB's remote
                // object.
                System.out.println(s.getAllState().toString());
            }
        } catch (FullException e) {
            fail();
        }
    }
}
