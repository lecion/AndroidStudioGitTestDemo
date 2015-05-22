package com.yliec.host;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;


public class MainActivity extends ActionBarActivity {
    TextView tvPlugin;
    TextView tvPluginName;
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPlugin = (TextView) findViewById(R.id.tv_plugin);
        tvPluginName = (TextView) findViewById(R.id.tv_plugin_name);
        tvResult = (TextView) findViewById(R.id.tv_result);
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent("com.yliec.plugin.client");
        List<ResolveInfo> plugins = packageManager.queryIntentActivities(intent, 0);
        tvPlugin.setText("插件数量: " + plugins.size());
        for (ResolveInfo plugin : plugins) {
            tvPluginName.setText(plugin.resolvePackageName);
            tvPluginName.setText("插件名称" + plugin.activityInfo.packageName);
            ActivityInfo activityInfo = plugin.activityInfo;
            String packageName = activityInfo.packageName;
            String dexPath = activityInfo.applicationInfo.sourceDir;
            String dexOutputDir = getApplicationInfo().dataDir;
            String libPath = activityInfo.applicationInfo.nativeLibraryDir;
            tvPluginName.setText(String.format("插件包名: %s  \ndexPath: %s\ndexOutPath: %s\n libPath: %s", packageName, dexPath, dexOutputDir, libPath));
            DexClassLoader classLoader = new DexClassLoader(dexPath, dexOutputDir, libPath, this.getClassLoader());
            try {
                Class<?> clazz = classLoader.loadClass(packageName + ".PluginClass");
                Object obj = clazz.newInstance();
                Class[] params = new Class[2];
                params[0] = Integer.TYPE;
                params[1] = Integer.TYPE;
                Method method = clazz.getMethod("add", params);
                Integer result = (Integer) method.invoke(obj, 13, 66);
                tvResult.setText("13 + 66 = " + result);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
