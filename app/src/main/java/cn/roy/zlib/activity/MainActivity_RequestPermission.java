package cn.roy.zlib.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.roy.module.permission_ext.PermissionHelper;
import cn.roy.module.permission_ext.RequestPermissionContextHolder;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/08/05
 * @Version: v1.0
 */
public class MainActivity_RequestPermission implements RequestPermissionContextHolder {
    private Context context;

    public MainActivity_RequestPermission() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void camera(String path) {
        String[] permissions = {Manifest.permission.CAMERA};
        boolean autoApply = true;
        int applyPermissionCode = 50001;
        String applyPermissionTip = "这是代理方法，需要申请相机权限";
        String lackPermissionTip = "相机权限已被拒绝";
        String methodName = "camera";
        Class<?>[] methodParams = {String.class};
        boolean hasPermission = PermissionHelper.hasPermission(context, permissions);
        if (hasPermission) {
            try {
                Method method = context.getClass()
                        .getDeclaredMethod(methodName + "_real", methodParams);
                method.invoke(context, path);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return;
        }
        if (context instanceof Activity && autoApply) {
            Activity activity = (Activity) context;
            if (TextUtils.isEmpty(applyPermissionTip)) {
                new AlertDialog.Builder(context)
                        .setMessage(applyPermissionTip)
                        .setPositiveButton("确定", (dialog, which) -> {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(activity, permissions,
                                    applyPermissionCode);
                        }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }
            ActivityCompat.requestPermissions(activity, permissions, applyPermissionCode);
        }
        Toast.makeText(context, lackPermissionTip, Toast.LENGTH_SHORT).show();
    }

}
