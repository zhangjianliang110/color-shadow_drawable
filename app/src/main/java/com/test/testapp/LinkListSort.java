package com.test.testapp;

import org.w3c.dom.Node;

import java.util.Stack;

/**
 * Description:
 * Created by zhangjianliang on 2019/12/6
 */
public class LinkListSort {

    public static class Node {

        private int data;

        private Node next;

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        public int getData() {
            return data;
        }
    }

    /** 单链表翻转 */
    public static Node reverseSingleLinkedList(Node head) {
        Node p, q, r;
        p = head;
        q = head.next;
        head.next = null;
        while (q != null) {
            r = q.next;
            q.next = p;
            p = q;
            q = r;
        }
        head = p;
        return head;
    }
    /**
     * 找出中间节点
     */
    public static Node findMiddleNode(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 删除倒数第 K 个节点
     */
    public static Node deleteLastKth(Node head, int k) {
        Node slow = head;
        Node fast = head;
        for (int i = 0; i < k; i++) {//快指针先走 K 步
            if (fast == null) {
                break;
            }
            fast = fast.next;
        }
        if (fast == null) {//总长不足 K
            return head;
        }
        Node pre = null;
        while (fast.next != null) {
            fast = fast.next;
            pre = slow;
            slow = slow.next;
        }
        /* prev == null 说明 fast走完 k 步后，正好走完了，slow一步都没走
         * 链表总长度刚好是 k，倒数第k个元素就是 head
         */
        if (pre == null) {
            head = head.next;
        } else {//删除 slow 节点
            pre.next = pre.next.next;
        }
        return head;
    }

    /**
     * 合并两个有序链表
     */
    public static Node mergeTwoLists(Node nodeA, Node nodeB){
        Node head = null;
        if (nodeA.data > nodeB.data) {
            head = nodeB;
            head.next = mergeTwoLists(nodeB.next, nodeA);
        } else {
            head = nodeA;
            head.next = mergeTwoLists(nodeA.next, nodeB);
        }
        return head;
    }

    /**
     * 检测环，返回环的入口节点
     */
    public static Node findLoopPoint(Node head) {
        Node fast = head;
        Node slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                break;
            }
        }
        if (fast == null || fast.next == null) {
            return null;
        }
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }
    /**
     * 两个单链表相交的起始节点
     */
    public static Node getIntersectionNode(Node headA, Node headB) {
        int sizeA = getSize(headA);
        int sizeB = getSize(headB);
        int distance = Math.abs(sizeA - sizeB);
        if (sizeA > sizeB) {
            for (int i = 0; i < distance; i++) {
                headA = headA.next;
            }
        } else {
            for (int i = 0; i < distance; i++) {
                headB = headB.next;
            }
        }
        Node crossNode = null;
        while (headA != null && headB != null) {
            if (headA == headB) {
                crossNode = headA;
                break;
            }
            headA = headA.next;
            headB = headB.next;
        }
        return crossNode;
    }



    private static int getSize(Node node) {
        int size = 0;
        while (node != null) {
            node = node.next;
            size++;
        }
        return size;
    }

    /**
     * 奇偶链表
     */
    public static Node oddEvenList(Node head) {
        Node oddNode = head;
        Node evenNode = head.next;
        Node evenHead = evenNode;
        while (evenNode != null && evenNode.next != null) {
            oddNode.next = evenNode.next;
            oddNode = oddNode.next;
            evenNode.next = oddNode.next;
            evenNode = evenNode.next;
        }
        oddNode.next = evenHead;
        return head;
    }

    /**
     * 回文链表
     */
    public boolean isPalindrome(Node head){
        Stack<Integer> stack = new Stack<>();
        Node fast = head;
        Node slow = head;
        while(head.next != null && head.next.next != null) {
            stack.push(slow.data);
            fast = fast.next.next;
            slow = slow.next;
        }
        if (fast != null) {//链表长度奇数
            slow = slow.next;
        }
        while (slow != null) {
            if (stack.pop() != slow.data) {
                return false;
            }
            slow = slow.next;
        }
        return stack.isEmpty();
    }
}
