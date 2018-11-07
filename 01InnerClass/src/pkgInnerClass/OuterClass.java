/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgInnerClass;

/**
 *
 * @author Gerald
 */
public class OuterClass {

    private int x = 99;

    public OuterClass() {
        System.out.println("constructor...x=" + x);
    }

    @Override
    public String toString() {
        int inString = 111;
        class LocalClass {

            private int y = 999;

            /* static not allowed */
            public String xy() {
                x += inString;
                return ("[local class] x+y=" + x + "+" + y);
            }
        }
        LocalClass lc = new LocalClass();
        //inString++;
        return "toString() in OuterClass; " + lc.xy() + "; " + lc.getClass();
    }

    public class InnerClass {

        private int y = 101;

        /* static not allowed */
        public void xy() {
            x++;
            System.out.println("[inner class] x+y=" + x + "+" + y);
        }
    }

}
