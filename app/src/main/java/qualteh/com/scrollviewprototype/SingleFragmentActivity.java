// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity
{

    public SingleFragmentActivity()
    {
    }

    protected abstract Fragment createFragment();

    protected int getLayoutResId()
    {
        return R.layout.activity_main;
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(getLayoutResId());
    }
}
