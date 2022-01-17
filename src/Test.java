public class Test {

    private static final double g = -9.81;

    public static void main(String[] args) {
        // Obj ob = new Obj(new Vector(50.0, 50.0), new Vector(-10.0, 25.0),new Vector(0,g), 5.0, 1.0);
        // Obj ob2 = new Obj(new Vector(30.0, 30.0), new Vector(5.0, 5.0), 5.0);
        Obj[] obs = new Obj[100];
        // obs[0] = ob;
        double[] rRange = { 2.0, 4.0 };
        double[] vRange = { 40.0, 60.0 };
        double[] massRange = { 1.0, 5.0 };
        double[] aRange = { -9.81, -9.81 };
        genObs(obs, 100, rRange, vRange, massRange);
        // genObsAcc(obs, aRange);
        // System.out.println(obs[0]);
        // Obj[] obs = ObjMaker.makeFromFile("src/saved3.txt");

        // Obj[] obs = { ob };
        Sim s = new Sim(obs);
        s.setMassRange(massRange);
        // s.setTps(2.0);
        // s.setMaxTime(20.0);
        s.start();
    }

    public static void genObs(Obj[] obs, int xyrange, double[] rRange, double[] vRange, double[] massRange) {
        for (int i = 0; i < obs.length; i++) {
            Vector pos = new Vector(Math.random() * xyrange, Math.random() * xyrange);
            Vector vel = new Vector(Math.random() * (vRange[1] - vRange[0]) + vRange[0],
                    Math.random() * (vRange[1] - vRange[0]) + vRange[0]);
            double r = Math.random() * (rRange[1] - rRange[0]) + rRange[0];
            double mass = Math.random() * (massRange[1] - massRange[0]) + massRange[0];
            obs[i] = new Obj(pos, vel, r, mass);

            for (int j = 0; j < i; j++)
                if (Obj.collide(obs[i], obs[j])) {
                    i--;
                    break;
                }

            if (Obj.collideWall(obs[i], 100))
                i--;
        }
        for (int i = 0; i < obs.length; i++)
            System.out.println(obs[i]);
    }

    public static void genObsPos(Obj[] obs, int xyrange) {
        for (int i = 0; i < obs.length; i++) {
            Vector pos = new Vector(Math.random() * xyrange, Math.random() * xyrange);
            obs[i].setPos(pos);
        }
    }

    public static void genObsVel(Obj[] obs, double[] rRange) {
        for (int i = 0; i < obs.length; i++) {
            double r = Math.random() * (rRange[1] - rRange[0]) + rRange[0];
            obs[i] = new Obj(obs[i].getPos().clone(), obs[i].getVel().clone(), obs[i].getAcc().clone(), r,
                    obs[i].getMass());
        }

    }

    public static void genObsAcc(Obj[] obs, double[] aRange) {
        for (int i = 0; i < obs.length; i++) {
            Vector acc = new Vector(0,
                    Math.random() * (aRange[1] - aRange[0]) + aRange[0]);
            obs[i].setAcc(acc);
        }
    }
}
