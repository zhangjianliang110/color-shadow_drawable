package com.test.testapp;

import java.util.Arrays;

/**
 * Description:
 * Created by zhangjianliang on 2019/12/6
 */
public class SortTest {

    /**
     * 冒泡排序
     */
    public static void bubbleSort(int[] array) {
        int len = array.length;
        for (int out = len - 1; out > 1; out--) {
            for (int in = 0; in < out; in++) {
                if (array[in] > array[in + 1]) {
                    int temp = array[in];
                    array[in] = array[in + 1];
                    array[in + 1] = temp;
                }
            }
        }
    }

    /**
     * 选择排序
     */
    public static void selectSort(int[] array) {
        int len = array.length;
        int minIdx;
        for (int out = 0; out < len - 1; out++) {
            minIdx = out;
            for (int in = 1; in < len; in++) {
                if (array[in] < array[minIdx]) {
                    minIdx = in;
                }
                int temp = array[out];
                array[out] = array[minIdx];
                array[minIdx] = temp;
            }
        }
    }

    /**
     * 插入排序
     */
    public static void insertSort(int[] array) {
        int len = array.length;
        for (int out = 1; out < len; out++) {
            int temp = array[out];
            int in = out;
            while (in > 0 && temp < array[in - 1]) {
                array[in] = array[in - 1];
                in--;
            }
            array[in] = temp;
        }
    }

    /**
     * 二分插入法
     */
    public static void binaryInsertSort(int[] array) {
        int len = array.length;
        for (int out = 1; out < len; out++) {
            int left = 0;
            int right = out - 1;
            int middle = -1;
            int temp = array[out];
            while (left <= right) {
                middle = left + (right - left) / 2;
                if (temp > array[middle]) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }
            for (int i = out - 1; i >= left; i--) {
                array[i + 1] = array[i];
            }
            array[left] = temp;
        }
    }

    /**
     * 快速排序
     */
    public static void fastSort(int[] array) {
        int len = array.length;
        int left = 0;
        int right = len - 1;
        int middle = left + (right - left) / 2;
        int anchor = array[middle];
        while (left <= right) {
            while (array[left] < anchor) {
                left++;
            }
            while (array[right] > anchor) {
                right--;
            }
            if (left < right) {
                int temp = array[left];
                array[left] = array[right];
                array[right] = temp;
                left++;
                right--;
            } else if (left == right) {
                left++;
            }
        }
    }

    /**
     * 希尔排序
     */
    public static void shellSort(int[] array) {
        // TODO: 2019/12/6
    }

    /**
     * 基数排序
     */
    public static void radixSort(int[] array) {
        // TODO: 2019/12/6
    }

    /**
     * 堆排序
     */
    public static void heapSort(int[] array) {
        int len = array.length;
        int beginIdx = len / 2 - 1;//最后一个非叶子节点
        for (int i = beginIdx; i >= 0; i--) {
            buildMaxHeap(array, i, len);
        }
        for (int i = len - 1; i > 0; i--) {
            swap(array, 0, i);
            buildMaxHeap(array, 0, i - 1);
        }
    }

    private static void swap(int[] array, int left, int right) {
        int temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }

    private static void buildMaxHeap(int[] array, int currIdx, int lastIdx) {
        int leftChildIdx = currIdx * 2 + 1;//左子节点索引
        int rightChildIdx = leftChildIdx + 1;
        int maxChildIdx = leftChildIdx;
        if (leftChildIdx > lastIdx) {
            return;
        }
        if (rightChildIdx <= lastIdx && array[rightChildIdx] > array[maxChildIdx]) {
            maxChildIdx = rightChildIdx;
        }
        if (array[maxChildIdx] > array[currIdx]) {
            swap(array, maxChildIdx, currIdx);
            buildMaxHeap(array, maxChildIdx, lastIdx);
        }
    }


    /**
     * 归并排序
     */
    public static void mergeSort(int[] array) {
        int len = array.length;
        sort(array, 0, len - 1);
    }

    private static void sort(int[] array, int left, int right) {
        if (left == right) {
            return;
        }
        int middle = left + (right - left) / 2;
        sort(array, left, middle);
        sort(array, middle + 1, right);
        merge(array, left, middle, right);
    }

    private static void merge(int[] array, int left, int middle, int right) {
        int[] temp = new int[right - left + 1];
        int tempIdx = 0;
        int leftStart = left;
        int rightStart = middle + 1;
        while (leftStart < middle && rightStart < right) {
            if (array[leftStart] < array[rightStart]) {
                temp[tempIdx] = array[leftStart];
                tempIdx++;
                leftStart++;
            } else {
                temp[tempIdx] = array[rightStart];
                tempIdx++;
                rightStart++;
            }
        }
        while (leftStart <= middle) {
            temp[tempIdx] = array[leftStart];
            leftStart++;
            tempIdx++;
        }
        while (rightStart <= right) {
            temp[tempIdx] = array[rightStart];
            tempIdx++;
            rightStart++;
        }
        for (int i = 0; i < temp.length; i++) {
            array[left + i] = temp[i];
        }
    }

    public static void test(int[] array) {
        Arrays.sort(array);
    }

}
