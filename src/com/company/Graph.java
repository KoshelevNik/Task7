package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Graph {
    private final ArrayList<ArrayList<Boolean>> adjacencyMatrix = new ArrayList<>();
    private final ArrayList<Integer> vertexes = new ArrayList<>();

    public void addEdge(int v1, int v2) {
        if ((vertexes.size() != 0)) {
            addVertex(v1);
        } else {
            addFirstVertex(v1);
        }
        addVertex(v2);
        adjacencyMatrix.get(vertexes.indexOf(v1)).set(vertexes.indexOf(v2), true);
        adjacencyMatrix.get(vertexes.indexOf(v2)).set(vertexes.indexOf(v1), true);
    }

    private void addFirstVertex(int v) {
        vertexes.add(v);
        ArrayList<Boolean> newString = new ArrayList<>();
        newString.add(false);
        adjacencyMatrix.add(newString);
    }

    public void addVertex(int v) {
        if (!vertexes.contains(v)) {
            vertexes.add(v);
            for (ArrayList<Boolean> x : adjacencyMatrix) {
                x.add(false);
            }
            ArrayList<Boolean> newString = new ArrayList<>();
            for (Boolean ignored : adjacencyMatrix.get(0)) {
                newString.add(false);
            }
            adjacencyMatrix.add(newString);
        }
    }

    public boolean connectedness() {
        boolean[][] ask = new boolean[adjacencyMatrix.size()][adjacencyMatrix.size()];
        for (int i = 0; i < adjacencyMatrix.size(); i++) {
            for (int j = 0; j < adjacencyMatrix.get(i).size(); j++) {
                ask[i][j] = adjacencyMatrix.get(i).get(j);
            }
        }
        return connectedness(ask, ask, 1);
    }

    private boolean connectedness(boolean[][] ask, boolean[][] m, int i) {
        if (i == adjacencyMatrix.size()) {
            for (boolean[] v : ask) {
                for (boolean x : v) {
                    if (!x) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            boolean[][] newM = new boolean[m.length][m.length];
            for (int a = 0; a < newM.length; a++) {
                for (int z = 0; z < newM.length; z++) {
                    boolean value = false;
                    for (int j = 0; j < m.length; j++) {
                        value = value || (m[a][j] && adjacencyMatrix.get(j).get(z));
                    }
                    newM[a][z] = value;
                }
            }
            for (int j = 0; j < m.length; j++) {
                for (int z = 0; z < newM.length; z++) {
                    ask[j][z] = ask[j][z] || newM[j][z];
                }
            }
            return connectedness(ask, newM, i + 1);
        }
    }

    public JComponent draw(int x, int y) {
        return new Draw(x, y);
    }

    private class Draw extends JComponent {
        int[][] coordinates = new int[vertexes.size()][2];

        Draw(int x, int y) {
            setLayout(null);
            setSize(vertexes.size() * 50, vertexes.size() * 50);
            setLocation(x, y);
            int size = 0;
            for (Integer v : vertexes) {
                int xVert = (int) (Math.random() * (getWidth() - 35));
                int yVert = (int) (Math.random() * (getHeight() - 35));
                while (contains(coordinates, size, xVert, yVert)) {
                    xVert = (int) (Math.random() * (getWidth() - 35));
                    yVert = (int) (Math.random() * (getHeight() - 35));
                }
                coordinates[size++] = new int[]{xVert, yVert};
                Vertex vertex = new Vertex(v);
                add(vertex);
                vertex.setLocation(xVert, yVert);
                vertex.setSize(35, 35);
            }
        }

        private boolean contains(int[][] list, int size, int v1, int v2) {
            for (int i = 0; i < size; i++) {
                if (
                        v1 > list[i][0] - 45 &&
                                v1 < list[i][0] + 45 &&
                                v2 > list[i][1] - 45 &&
                                v2 < list[i][1] + 45
                ) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int i = 0; i < vertexes.size(); i++) {
                for (int j = i + 1; j < adjacencyMatrix.size(); j++) {
                    if (adjacencyMatrix.get(i).get(j)) {
                        if (coordinates[i][1] > coordinates[j][1] + 35) {
                            g.drawLine(
                                    coordinates[i][0] + 17,
                                    coordinates[i][1],
                                    coordinates[j][0] + 17,
                                    coordinates[j][1] + 35
                            );
                        } else if (coordinates[j][1] > coordinates[i][1] + 35) {
                            g.drawLine(
                                    coordinates[i][0] + 17,
                                    coordinates[i][1] + 35,
                                    coordinates[j][0] + 17,
                                    coordinates[j][1]
                            );
                        } else if (coordinates[i][0] > coordinates[j][0] + 35) {
                            g.drawLine(
                                    coordinates[i][0],
                                    coordinates[i][1] + 17,
                                    coordinates[j][0] + 35,
                                    coordinates[j][1] + 17
                            );
                        } else if (coordinates[i][0] < coordinates[j][0] - 35) {
                            g.drawLine(
                                    coordinates[i][0] + 35,
                                    coordinates[i][1] + 17,
                                    coordinates[j][0],
                                    coordinates[j][1] + 17
                            );
                        }
                    }
                }
            }
        }

        private class Vertex extends JComponent {
            int v;

            Vertex(int v) {
                this.v = v;
            }

            @Override
            public void paint(Graphics g) {
                g.setColor(new Color(255, 255, 255));
                g.fillRect(0, 0, 35, 35);
                g.setColor(new Color(0, 0, 0));
                g.drawRect(0, 0, 34, 34);
                g.setFont(new Font("LOL", Font.ITALIC, 20));
                g.drawString(String.valueOf(v), (35 - String.valueOf(v).length()) / 2, 20);
            }
        }
    }
}
