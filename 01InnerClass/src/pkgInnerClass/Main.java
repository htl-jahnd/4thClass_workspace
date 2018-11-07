/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgInnerClass;

import java.util.TreeSet;

/**
 *
 * @author Gerald
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		System.out.println("=...new OuterClass...");
		OuterClass oc = new OuterClass();
		System.out.println("=...new InnerClass...");
		OuterClass.InnerClass ic = oc.new InnerClass();
		System.out.println("=...2nd new InnerClass...");
		OuterClass.InnerClass iic = new OuterClass().new InnerClass();
		System.out.println("=...2nd new OuterClass...");
		OuterClass ooc = new OuterClass() {
			@Override
			public String toString() {
				return "x2==unknown";
			}
		};
		System.out.println("=...toString Outerclass...");
		System.out.println("=" + oc.toString() + "," + oc.getClass().toString());
		System.out.println("=...toString Innerclass...");
		System.out.println("=" + ic.toString() + "," + ic.getClass().toString());
		System.out.println("=...toString Outerclass-new...");
		System.out.println("=" + ooc.toString() + "," + ooc.getClass().toString());
		System.out.println("=" + (ic instanceof OuterClass.InnerClass ? "is instance" : "is not"));
		System.out.println("=" + (ooc instanceof OuterClass ? "is instance" : "is not"));
		// Syntax:
		// System.out.println(ic instanceof OuterClass?"is instance":"is not");

	}

}
