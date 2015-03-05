package pl.treefrog.phobos.exception;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class PhobosAssert {

    public static void assertNotNull(String msg,Object obj) throws PlatformException {
        if (obj == null){
            throw new PlatformException(msg);
        }
    }
}
