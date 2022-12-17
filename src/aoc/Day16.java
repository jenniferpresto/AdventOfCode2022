package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day16 {
    static final int NUM_MINUTES = 30;
//    static AtomicInteger HIGHEST_FLOW = new AtomicInteger();
    static int HIGHEST_FLOW_INT = 0;
    static int NUM_FINISHED_PATHS = 0;

    private static class RoomPair {
        final String room1;
        final String room2;
        RoomPair(String room1, String room2) {
            this.room1 = room1;
            this.room2 = room2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            RoomPair other = (RoomPair) o;
            return this.room1.equals(other.room1) && this.room2.equals(other.room2);
        }
        boolean isEquivalent(RoomPair other) {
            return (this.room1.equals(other.room1) && this.room2.equals(other.room2)) ||
                    (this.room1.equals(other.room2) && this.room2.equals(other.room1));
        }
    }
    private static class CaveRoom {
        final String name;
        final int flow;
        final List<String> childNames;
        final List<CaveRoom> children;
        boolean valveOpen = false;

        CaveRoom(String name, int flow) {
            this.name = name;
            this.flow = flow;
            this.childNames = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        @Override
        public String toString() {
            String openString = this.valveOpen ? "open" : "shut";
            String output = name + "(" + openString + "): flow: " + flow + ", children: ";
            for (String childName : childNames) {
                output += childName + " ";
            }
            return output;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            CaveRoom other = (CaveRoom)o;
            return this.name.equals(other.name) &&
                    this.flow == other.flow &&
                    this.childNames.equals(other.childNames) &&
                    this.children.equals(other.children);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.flow, this.childNames);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day16.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        //  map out cave system
        List<CaveRoom> caveRooms = new ArrayList<>();
        Map<String, Integer> flowMap = new HashMap<>();
        for (String line : data) {

            String[] instr = line.split(" ");
            String name = instr[1];
            String flowStr = instr[4].split("=")[1];
            int flow = Integer.valueOf(flowStr.substring(0, flowStr.length() - 1));
            flowMap.put(name, flow);
            CaveRoom newRoom = new CaveRoom(name, flow);

            for (int i = 9; i < instr.length; i++) {
                if (instr[i].endsWith(",")) {
                    newRoom.childNames.add(instr[i].substring(0, instr[i].length() -1));
                } else {
                    newRoom.childNames.add(instr[i]);
                }
            }
            caveRooms.add(newRoom);
        }
        //  link 'em up (there's a more efficient way to do this)
        for (CaveRoom room : caveRooms) {
            for (String childName : room.childNames) {
                for (CaveRoom subRoom : caveRooms) {
                    if (subRoom.name.equals( childName)) {
                        room.children.add(subRoom);
                        break;
                    }
                }
            }
        }


        //  Tree traversal
        CaveRoom head = caveRooms.get(0);
        String[] path = new String[NUM_MINUTES];
        traverseRooms(head, path, 0, 0, caveRooms);
        System.out.println("Highest flow: " + HIGHEST_FLOW_INT);
        System.out.println("Number of finished paths: " + NUM_FINISHED_PATHS);
    }

    static void traverseRooms(CaveRoom currentRoom, String[] path, int idx, int totalFlow, List<CaveRoom> caveRooms) {
        boolean isTestPath = false;
        //  clear out path so it's easier to read
        for (int i = 0; i < path.length; i++) {
            if (i > idx - 1) {
                path[i] = null;
            }
        }

        if (
                path[0] != null && path[0].equals("->DD")
//                && path[1] != null && path[1].equals("open DD")
//                && path[2] != null &&  path[2].equals("->CC")
//                && path[3] != null && path[3].equals("->BB")
//                && path[4] != null && path[4].equals("open BB")
//                && path[5] != null && path[5].equals("->AA")
//                && path[6] != null && path[6].equals("->II")
//                && path[7] != null && path[7].equals("->JJ")
//                && path[8] != null && path[8].equals("open JJ")
//                && path[9] != null && path[9].equals("->II")
//                && path[10] != null && path[10].equals("->AA")
//                && path[11] != null && path[11].equals("->DD")
//                && path[12] != null && path[12].equals("->EE")
//                && path[13] != null && path[13].equals("->FF")
//                && path[14] != null && path[14].equals("->GG")
//                && path[15] != null && path[15].equals("->HH")
//                && path[16] != null && path[16].equals("open HH")
//                && path[17] != null && path[17].equals("->GG")
//                && path[18] != null && path[18].equals("->FF")
//                && path[19] != null && path[19].equals("->EE")
//                && path[20] != null && path[20].equals("open EE")
//                && path[21] != null && path[21].equals("->DD")
//                && path[22] != null && path[22].equals("->CC")
//                && path[23] != null && path[23].equals("open CC")
        )
        {
            String pathStr = getPathString(path, idx);
//            System.out.println("HItting test path: " );
            isTestPath = true;
        }

        String pathString = getPathString(path, idx);
        String testPathString = "->DD, open DD, ->CC, ->BB, open BB, ->AA, ->II, ->JJ, open JJ, ->II, ->AA, ->DD, ->EE, ->FF, ->GG, ->HH, open HH";
        if (testPathString.startsWith(pathString)) {
            boolean willThisWork = true;
        }

        //  reset all the valves for this path
        Set<String> openCaves = new HashSet<>();
        for (CaveRoom freshRoom : caveRooms) {
            freshRoom.valveOpen = false;
            for (int i = 0; i < idx; i++) {
                if (path[i].startsWith("open")) {
                    String openCave = path[i].split(" ")[1];
                    CaveRoom cave = getCaveByName(openCave, caveRooms);
                    cave.valveOpen = true;
                    openCaves.add(openCave);
                }
            }
        }

        //  add up flow for open caves
        int flowPerMinute = 0;
        for (CaveRoom caveRoom : caveRooms) {
            if (caveRoom.valveOpen) {
                flowPerMinute += caveRoom.flow;
            }
        }
        if (isTestPath) {
            System.out.println(flowPerMinute);
//            if (flowPerMinute == 81) {
//                System.out.println(getPathString(path, idx));
//            }
        }


        //  see if all valves are already open; if so, can stop traversing rooms
        boolean allOpen = true;
        for (CaveRoom room : caveRooms) {
            if(room.flow > 0 && !room.valveOpen) {
                allOpen = false;
            }
        }
        if (allOpen) {
            //  add the remaining flow and call it a day
//            System.out.println("ALL OPEN!!!!");
            totalFlow += (NUM_MINUTES - idx) * flowPerMinute;
            if (totalFlow > HIGHEST_FLOW_INT) {
                HIGHEST_FLOW_INT = totalFlow;
            }
            NUM_FINISHED_PATHS++;
            return;
        }
        totalFlow += flowPerMinute;

        if (idx > NUM_MINUTES - 1) {
            if (totalFlow == 1718) {
                System.out.println("Highest flow path: " + getPathString(path, idx));
            }
            if (totalFlow > HIGHEST_FLOW_INT) {
                HIGHEST_FLOW_INT = totalFlow;
            }
            System.out.println("Finished path: " + getPathString(path, path.length));
            NUM_FINISHED_PATHS++;
            return;
        }

        if (isInefficient(path, idx, caveRooms)) {
//            System.out.println("Abandoning at " + idx + ": " + getPathString(path, idx));
            path[idx] = "inefficient";
            return;
        }

        //  if we haven't hit an end state, do it again:
        //*********** SUSPECT PROBLEM IS HERE
        //  continue traversing rooms
        if (path[idx] != null) {
            int jennifer = 9;
        }
        if (!currentRoom.valveOpen && currentRoom.flow > 0) {
            //  we need to look at both paths if it could potentially be opened
            String[] openPath = path.clone();
            String[] closedPath = path.clone();

            //  open
            openPath[idx] = "open " + currentRoom.name;
            traverseRooms(currentRoom, openPath, idx + 1, totalFlow, caveRooms);
            for (CaveRoom child : currentRoom.children) {
                closedPath[idx] = "->" + child.name;
                traverseRooms(child, closedPath, idx + 1, totalFlow, caveRooms);
            }
        } else {
            for (CaveRoom child : currentRoom.children) {
                path[idx] = "->" + child.name;
                traverseRooms(child, path, idx + 1, totalFlow, caveRooms);
            }
        }
        //*********** END SUSPECT PROBLEM IS HERE
    }

    static CaveRoom getCaveByName(String name, List<CaveRoom> caveRooms) {
        for (CaveRoom room : caveRooms) {
            if (name.equals(room.name)) {
                return room;
            }
        }
        System.out.println("This shouldn't happen");
        return null;
    }

    static CaveRoom getCaveFromPathEntry(String pathEntry, List<CaveRoom> caveRooms) {
        String name = "";
        if (pathEntry.startsWith("->")) {
            name = pathEntry.substring(2);
        } else if (pathEntry.startsWith("open ")) {
            name = pathEntry.substring(5);
        }
        for (CaveRoom room : caveRooms) {
            if (room.name.equals(name)) {
                return room;
            }
        }
        System.out.println("This shouldn't happen");
        return null;
    }

    //  if we walk between the same two rooms without opening any valves,
    //  we're being inefficient -- no time for that!
    static boolean isInefficient(String[] path, int idx, List<CaveRoom> caveRooms) {
        int end = idx - 1;
        int start = end;
        Set<CaveRoom> caveRoomsSinceLastOpenedValve = new HashSet<>();
        if (end < 0) return false;
        String lastStep = path[end];

        //  some efficiency checks based on valves
        int biggestUnopenedValved = caveRooms.stream()
                .filter(r -> !r.valveOpen)
                .mapToInt(r -> r.flow)
                .max().orElse(0);


        //  if we just left a room, and it has the biggest valve,
        //  and we still didn't open it, this path is inefficient
        if (end > 2
                && path[end - 1].startsWith("->")
                && path[end].startsWith("->")) {
            CaveRoom vacatedRoom = getCaveFromPathEntry(path[end-1], caveRooms);
            if (vacatedRoom.flow == biggestUnopenedValved) {
                System.out.println("What are you waiting for to open this valve? biggest unopened: " + biggestUnopenedValved + ": " + getPathString(path, idx));
                return true;
            }
        }

        //  if we just opened a valve, but we visited that room in the past
        //  AND there isn't a bigger unopened valve left, this path is inefficient
        if (end > 2 && lastStep.startsWith("open")) {
            CaveRoom roomToTest = getCaveFromPathEntry(lastStep, caveRooms);
            if (roomToTest.flow >= biggestUnopenedValved) {
                for (int i = end - 2; i > -1; i--) {
                    if (path[i].contains(roomToTest.name)) {
//                    System.out.println("Inefficient because we've been here before!" + getPathString(path, idx));
                        String pathString = getPathString(path, end);
                        return true;
                    }
                }
            }
        }

        //  go backwards down the path until we last opened a valve
        //  find the last time we opened a valve, or the beginning of the path
        for (int i = end - 1; i > -1; i--) {
            if (path[i].startsWith("open")) {
                start = i;
                break;
            } else if (i == 0) {
                start = 0;
            }
        }

        if (end - start < 2) {
            return false;
        }

        for (int i = start; i < end; i++) {
            CaveRoom cave = getCaveFromPathEntry(path[i], caveRooms);
            if (caveRoomsSinceLastOpenedValve.contains(cave)) {
                return true;
            } else {
                caveRoomsSinceLastOpenedValve.add(cave);
            }
        }

//        for (int i = end - 1; i > -1; i--) {
//            if(path[i].startsWith("open")) {
//                start = i + 1;
//                String roomName = path[i].split(" ")[1];
////                if (roomsSinceLastOpenedValve.contains(roomName)) {
//////                    System.out.println("Inefficient, we've been here before (stop on open)! " + getPathString(path, idx));
////                    String pathString = getPathString(path, idx);
////                    return true;
////                }
//                if (caveRoomsSinceLastOpenedValve.contains())
//                roomsSinceLastOpenedValve.add(roomName);
//                break;
//            } else {
//                String roomName = path[i].substring(2);
//                if (roomsSinceLastOpenedValve.contains(roomName)) {
////                    System.out.println("Inefficient, we've been here before (don't stop on open)! " + getPathString(path, idx));
//                    String pathString = getPathString(path, idx);
//                    return true;
//                }
//                roomsSinceLastOpenedValve.add(path[i].substring(2));
//            }
//            if (i == 0) {
//                start = i;
//            }
//        }

        return false;
    }

    static String getPathString(String[] path, int idx) {
        String pathStr = "";
        for (int i = 0; i < idx; i++) {
            pathStr += path[i];
            if (i < idx - 1) {
                pathStr +=", ";
            }
        }
        return pathStr;
    }
}
