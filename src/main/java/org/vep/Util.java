package org.vep;

/**
 * Created by mw on 11/13/15.
 */
public class Util {

    /**
     * Attempts to check if current process is running in MATLAB JVM by checking for presence
     * of MATLAB specific classes.
     *
     * @return true if appear to run inside MATLAB; false otherwise
     */
    public static boolean runningInMATLAB()
    {
        // random classnames from jars distributed with MATLAB
        String[] classNames = new String[] {
                "com.mathworks.mwt.MWFrame",
                "com.mathworks.util.AbsoluteFile"
        };

        for (String className : classNames)
        {
            try
            {
                Class.forName(className, false, ClassLoader.getSystemClassLoader());
                return true;
            }
            catch (ClassNotFoundException e) {
                continue;
            }
        }

        return false;
    }
}
