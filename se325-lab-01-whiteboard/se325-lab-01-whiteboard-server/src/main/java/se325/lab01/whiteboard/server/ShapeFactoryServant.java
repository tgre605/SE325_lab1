package se325.lab01.whiteboard.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import se325.lab01.whiteboard.common.FullException;
import se325.lab01.whiteboard.common.Graphic;
import se325.lab01.whiteboard.common.Shape;
import se325.lab01.whiteboard.common.ShapeFactory;


/**
 * An implementation of the ShapeFactory interface. A ShapeFactoryServant
 * instance is a remotely accessible object that creates and stores
 * references to remotely accessible Shape objects. Within a client/server
 * application, a single ShapeFactoryServant object runs on the server; the
 * Shape objects created by the factory also reside on the server. Clients
 * acquire remote references to the Shape objects from the factory.
 */
public class ShapeFactoryServant extends UnicastRemoteObject implements ShapeFactory {

    private static final long serialVersionUID = 1L;

    private List<Shape> shapes;    // List of Shapes created a ShapeFactoryServant.
    private final int maxShapes;   // Capacity of a ShapeFactoryServant.

    /**
     * Creates a ShapeFactoryServant object.
     *
     * @param maxShapes the factory's capacity in terms of the maximum number
     *                  of shape objects that can be created.
     * @throws RemoteException if the server-side RMI run-time cannot create
     *                         the ShapeFactoryServant instance. Construction can fail if the RMI
     *                         runtime has insufficient resources to host the new object.
     */
    public ShapeFactoryServant(int maxShapes) throws RemoteException {
        super();
        shapes = new ArrayList<>();
        this.maxShapes = maxShapes;
    }

    /**
     * @see ShapeFactory#newShape(Graphic)
     */
    public synchronized Shape newShape(Graphic graphic) throws FullException, RemoteException {
        int numberOfShapes = shapes.size();

        if (numberOfShapes == maxShapes) {
            throw new FullException();
        }
        Shape newShape = new ShapeServant(graphic, numberOfShapes);
        shapes.add(newShape);
        return newShape;

    }

    /**
     * @see ShapeFactory#allShapes()
     */
    public synchronized List<Shape> allShapes() throws RemoteException {
        return shapes;
    }

    /**
     * @see ShapeFactory#shapes(int)
     */
    public synchronized List<Shape> shapes(int index) throws RemoteException {
        return shapes.subList(index, shapes.size() - 1);
    }

}
