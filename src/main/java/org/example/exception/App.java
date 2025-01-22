package org.example.exception;

class App {
    public static void main(String[] args) {
        View view = new View();
        try {
            System.out.println(view.doRender());
        } catch (ViewException e) {
            System.out.println("ERROR: " + unrollChain(e));
        }
    }

    static String unrollChain(Exception e) {
        Throwable current = e;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage();
    }
}

class View {
    private final Business business = new Business();

    String doRender() throws ViewException {
        try {
            return "<html>" + business.doBusiness() + "</html>";
        } catch (BusinessException e) {
            if (System.nanoTime() % 2 == 0)
                throw new ViewException("Some context if have one", e);
            else
                throw new ViewException(e); // no local context, pure rethrow
        }
    }
}

class Business {
    private final Dao dao = new Dao();

    int doBusiness() throws BusinessException {
        try {
            return dao.select() + 42;
        } catch (DaoException e) {
            if (System.nanoTime() % 2 == 0)
                throw new BusinessException("Some context if have one", e);
            else
                throw new BusinessException(e); // no local context, pure rethrow
        }
    }
}

class Dao {
    int select() throws DaoException {
        if (System.nanoTime() % 2 == 0)
            return 42;
        else
            throw new DaoException();
    }
}

class DaoException extends Exception {
}

class BusinessException extends Exception {
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}

class ViewException extends Exception {
    public ViewException(String message, Throwable cause) {
        super(message, cause);
    }
    public ViewException(Throwable cause) {
        super(cause);
    }
}