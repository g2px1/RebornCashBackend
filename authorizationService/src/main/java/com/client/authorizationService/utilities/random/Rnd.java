package com.client.authorizationService.utilities.random;

public class Rnd
{
    private static final MTRandom  rnd  = new MTRandom();

    /**
     * @return rnd
     *
     */
    public static float get() // get random number from 0 to 1
    {
        return rnd.nextFloat();
    }

    /**
     * Gets a random number from 0(inclusive) to n(exclusive)
     *
     * @param n
     *            The superior limit (exclusive)
     * @return A number from 0 to n-1
     */
    public static int get(int n)
    {
        return (int) Math.floor(rnd.nextDouble() * n);
    }

    /**
     * @param min
     * @param max
     * @return value
     */
    public static int get(int min, int max) // get random number from
    // min to max (not max-1 !)
    {
        return min + (int) Math.floor(rnd.nextDouble() * (max - min + 1));
    }

    /**
     * @param n
     * @return n
     */
    public static int nextInt(int n)
    {
        return (int) Math.floor(rnd.nextDouble() * n);
    }

    /**
     * @return int
     */
    public static int nextInt()
    {
        return rnd.nextInt();
    }

    /**
     * @return double
     */
    public static double nextDouble()
    {
        return rnd.nextDouble();
    }

    /**
     * @return double
     */
    public static double nextGaussian()
    {
        return rnd.nextGaussian();
    }

    /**
     * @return double
     */
    public static boolean nextBoolean()
    {
        return rnd.nextBoolean();
    }
}

/**
 * @author David Beaumont, Copyright 2005
 *         <p/>
 *         A Java implementation of the MT19937 (Mersenne Twister) pseudo random number generator algorithm based upon
 *         the original C code by Makoto Matsumoto and Takuji Nishimura (see <a
 *         href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html">
 *         http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html</a> for more information.
 *         <p/>
 *         As a subclass of java.util.Random this class provides a single canonical method next() for generating bits in
 *         the pseudo random number sequence. Anyone using this class should invoke the public inherited methods
 *         (nextInt(), nextFloat etc.) to obtain values as normal. This class should provide a drop-in replacement for
 *         the standard implementation of java.util.Random with the additional advantage of having a far longer period
 *         and the ability to use a far larger seed value.
 *         <p/>
 *         This is <b>not</b> a cryptographically strong source of randomness and should <b>not</b> be used for
 *         cryptographic systems or in any other situation where true random numbers are required.
 *         <p/>
 *         <!-- Creative Commons License --> <a href="http://creativecommons.org/licenses/LGPL/2.1/"><img
 *         alt="CC-GNU LGPL" border="0" src="http://creativecommons.org/images/public/cc-LGPL-a.png" /></a><br />
 *         This software is licensed under the <a href="http://creativecommons.org/licenses/LGPL/2.1/">CC-GNU LGPL</a>.
 *         <!-- /Creative Commons License --> <!-- <rdf:RDF xmlns="http://web.resource.org/cc/"
 *         xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"> <Work
 *         rdf:about=""> <license rdf:resource="http://creativecommons.org/licenses/LGPL/2.1/" /> <dc:type
 *         rdf:resource="http://purl.org/dc/dcmitype/Software" /> </Work> <License
 *         rdf:about="http://creativecommons.org/licenses/LGPL/2.1/"> <permits
 *         rdf:resource="http://web.resource.org/cc/Reproduction" /> <permits
 *         rdf:resource="http://web.resource.org/cc/Distribution" /> <requires
 *         rdf:resource="http://web.resource.org/cc/Notice" /> <permits
 *         rdf:resource="http://web.resource.org/cc/DerivativeWorks" /> <requires
 *         rdf:resource="http://web.resource.org/cc/ShareAlike" /> <requires
 *         rdf:resource="http://web.resource.org/cc/SourceCode" /> </License> </rdf:RDF> -->
 * @version 1.0
 */


// XORShift xorShift = new XORShift();
//		int counter = 0;
//		int counterMore90 = 0;
//		double percentageCount = 0.0D;
//		for (int j = 0; j < Math.pow(2, 3); j++) {
//			for (int i = 0; i < Math.pow(2, 10); i++) {
//				int rand = Rnd.get(0, 101);
////			int rand = xorShift.nextInt(101);
//				if (rand > 90) {
//					counterMore90++;
//				}
//				counter++;
////			System.out.print(Rnd.get(0, 101) + " ");
////			System.out.print(xorShift.nextInt(101) + " ");
//			}
//			System.out.println(counter);
//			System.out.println(counterMore90);
//			double percentage = (double) counterMore90 / counter;
//			percentageCount += percentage;
//			System.out.println(percentage);
//		}
//
//		System.out.println(String.format("Test result: %s", (double) percentageCount/Math.pow(2, 3)));