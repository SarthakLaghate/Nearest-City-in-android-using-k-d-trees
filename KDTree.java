package com.example.dsa;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.*;
import java.util.*;
import java.io.*;

class Node{
    static int k = 2;
    double point[]=new double[k]; // To store k dimensional point
    Node left, right;

    public Node(double arr[])
    {
        point[0]=arr[0];
        point[1]=arr[1];
        left=right=null;
    }

}
class KDT{

    static double currBest=Double.MAX_VALUE;

    static int k = 2;


    Node root;

    public KDT ()
    {
        root=null;

    }

    public void Root()
    {
        System.out.println(root.point[0]+" "+root.point[1]);
    }
    Node insertRec(Node root,double point[], int depth)
    {

        if (root == null)
            return (new Node(point));

        int cd = depth % k;

        if (point[cd] < (root.point[cd]))
            root.left  = insertRec(root.left, point, depth + 1);
        else
            root.right = insertRec(root.right, point, depth + 1);

        return root;
    }

    void insert(double point[])
    {
        root=insertRec(root, point, 0);

    }


    boolean arepointsSame(double point1[], double point2[])
    {

        if(point1[0]==point2[0]&&point1[1]==point2[1])
            return true;
        return false;
    }


    boolean searchRec(Node root, double point[], int depth)
    {

        if (root == null)
        {  return false; }
        if (arepointsSame(root.point, point))
            return true;

        int cd = depth % k;

        if (point[cd] < root.point[cd])
            return searchRec(root.left, point, depth + 1);

        return searchRec(root.right, point, depth + 1);
    }



    public Node nearest(Node root,double point[],Node currentBestNode,int depth)
    {
        if(root==null) return currentBestNode;
        if(dist(root.point,point)<currBest)
        {
            currBest=dist(root.point,point);
            currentBestNode=root;

        }
        Node good=null,bad=null;
        int cd = depth % k;
        if(point[cd]<root.point[cd])
        {
            good=root.left;
            bad=root.right;
        }
        else{
            bad=root.left;
            good=root.right;

        }

        currentBestNode=nearest(good, point, currentBestNode, depth+1);

        if(Math.abs(point[cd]-root.point[cd])<currBest)
            return currentBestNode=nearest(bad, point, currentBestNode, depth+1);

        return currentBestNode;


    }

    boolean search(double point[])
    {
        return searchRec(root, point, 0);
    }

    double dist(double []a,double b[])
    {
        return(Math.sqrt(Math.pow(a[0]-b[0],2)+Math.pow(a[1]-b[1],2)));
    }




    public double[] NearestNeighbour(double a[])
    {
        Node temp=nearest(root, a, root, 0);
        currBest=Double.MAX_VALUE;
        return temp.point;
    }


    public static double[] getNearestA(double[] input, Context mycontext)
    {
        Scanner sc=new Scanner(System.in);
        KDT kd=new KDT();

        JSONParser parser = new JSONParser();


        JSONObject mJsonObject=null;
        JSONArray mJsonArray = null;
        try {
            mJsonArray = new JSONArray((parser.parse(new BufferedReader
                    (new InputStreamReader(mycontext.getAssets().open("cities.json"), "UTF-8")))).toString());
        } catch (Exception e) {
            Log.d("DEBUG",e.getMessage()+ "  2");
            e.printStackTrace();
        }
        double[][] podoubles=new double[mJsonArray.length()][2];
        //Log.d("DEBUG", String.valueOf(mJsonArray.length()));
        for(int i=0;i<mJsonArray.length();i++){
            try {
                mJsonObject = mJsonArray.getJSONObject(i);
                podoubles[i][0]=Double.parseDouble(mJsonObject.getString("lat"));
                podoubles[i][1]=Double.parseDouble(mJsonObject.getString("lng"));
                kd.insert(podoubles[i]);
                //Log.d("DEBUG", String.valueOf(x[0])+"   "+String.valueOf(x[1]));
            } catch (JSONException e) {
                Log.d("DEBUG",e.getMessage());
                e.printStackTrace();
            }
        }
        kd.Root();
        double nnb[]=new double[2];
        nnb=kd.NearestNeighbour(input);
        return nnb;

    }

}