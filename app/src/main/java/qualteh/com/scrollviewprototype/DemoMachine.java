// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import java.util.Random;
import qualteh.com.scrollviewprototype.Model.Position;

// Referenced classes of package qualteh.com.scrollviewprototype:
//            Machine, GeoMath

public class DemoMachine extends Machine
{

    public double demoCoordinateX[] = {
        21.464092, 21.464815, 21.464179, 21.464206, 21.463884
    };
    public double demoCoordinateY[] = {
        46.321063, 46.320334, 46.320905, 46.321136, 46.320968
    };
    public int demoUICoordinateX[] = {
        270, 270, 330, 355
    };
    public int demoUICoordinateY[] = {
        750, 695, 695, 715
    };
    private int prevIndex;

    public DemoMachine()
    {
        prevIndex = -1;
    }

    public void newRandomCoordinate()
    {
        Random random = new Random();
        int i;
        do
        {
            i = random.nextInt(2);
        } while (i == prevIndex);
        setMachinePosition(GeoMath.calculatePosition(new Position(demoCoordinateX[i], demoCoordinateY[i])));
        getMachinePosition().calculateUIPosition();
        prevIndex = i;
    }
}
