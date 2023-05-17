package com.liminjun.test.someTask;

import cn.hutool.http.HttpUtil;

public class BenchMarkTask {

    static final int MOD = 1000000007;

    /**
     * 矩阵快速幂方法计算斐波那契数列，模拟计算密集型逻辑
     *
     * @param n n
     * @return int
     */
    public static int calFibonacci(int n) {
        if (n < 2) {
            return n;
        }
        int[][] q = {{1, 1}, {1, 0}};
        int[][] res = pow(q, n - 1);
        return res[0][0];
    }

    /**
     * 模拟io密集型逻辑
     *
     * @param url 请求地址
     * @return {@link String} 请求结果
     */
    public static String httpRequest(String url){
        return HttpUtil.get(url);
    }

    private static int[][] pow(int[][] a, int n) {
        int[][] ret = {{1, 0}, {0, 1}};
        while (n > 0) {
            if ((n & 1) == 1) {
                ret = multiply(ret, a);
            }
            n >>= 1;
            a = multiply(a, a);
        }
        return ret;
    }

    private static int[][] multiply(int[][] a, int[][] b) {
        int[][] c = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                c[i][j] = (int) (((long) a[i][0] * b[0][j] + (long) a[i][1] * b[1][j]) % MOD);
            }
        }
        return c;
    }

}