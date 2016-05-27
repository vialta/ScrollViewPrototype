// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import qualteh.com.scrollviewprototype.Model.Position;

public class GeoMath {

    public static Position calculatePosition(Position position) {
        Position position1 = new Position(0.0D, 0.0D);
        double d1 = 42D * 0.017453292519943295D;
        double d = Math.cos(d1);
        d1 = Math.sin(d1);
        position1.setGeoX(((position.getGeoX() - 21.464699D) * d - (position.getGeoY() - 46.320495999999999D) * d1) + 21.464699D);
        position1.setGeoY((position.getGeoX() - 21.464699D) * d1 + (position.getGeoY() - 46.320495999999999D) * d + 46.320495999999999D);
        position1.setGeoX(position1.getGeoX() + -0.371D * (position1.getGeoY() - 46.318998999999998D));
        position1.calculateUIPosition();
        return position1;
    }

}