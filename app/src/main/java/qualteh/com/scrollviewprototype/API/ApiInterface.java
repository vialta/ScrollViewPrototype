// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype.API;

import qualteh.com.scrollviewprototype.Model.MapModel;
import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface
{
    @POST("JsonTest.html")
    public Call<MapModel> callVersion ();
}
