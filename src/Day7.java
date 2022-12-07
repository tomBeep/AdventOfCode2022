import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Day7 {

    public static void main(String[] args) {
        new Day7().doChallenge();
    }

    private void doChallenge() {
        String input = getInput();
        Scanner sc = new Scanner(input);

        sc.nextLine();
        Node root = parseInputIntoTree(sc);

        doPart1(root);
        doPart2(root);
    }

    private void doPart1(Node root) {
        long limit = 100_000L;
        List<Node> nodesUnderSize = new ArrayList<>();
        root.getTotalSizeRecordingNodesUnderLimit(nodesUnderSize, limit);

        long totalUnderSizedNodes = nodesUnderSize.stream().mapToLong(n -> n.getTotalSize()).sum();
        System.out.printf("Part 1: Total size of all nodes under %,d is %,d\n", limit, totalUnderSizedNodes);
    }

    private void doPart2(Node root) {
        long totalSize = 70000000L;
        long required = 30000000L;
        long usedSpace = root.getTotalSize();
        long sizeToClear = (required - (totalSize - usedSpace));

        List<Node> nodesOverSizeToClear = new ArrayList<>();
        root.getTotalSizeRecordingNodesOverLimit(nodesOverSizeToClear, sizeToClear);

        long minSize = Long.MAX_VALUE;
        String minNodeName = null;
        for (Node node : nodesOverSizeToClear) {
            long nodeSize = node.getTotalSize();
            if (nodeSize < minSize) {
                minSize = nodeSize;
                minNodeName = node.name;
            }
        }
        System.out.printf("Part 2: The minimum sized node to remove is node %s at size %,d\n", minNodeName, minSize);
    }

    private Node parseInputIntoTree(Scanner sc) {
        Node root = new Node(null, "/");
        Node currentNode = root;

        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (isMoveIntoDir(line)) {
                String movingToName = line.split(" ")[2];
                Optional<Node> nodeToMoveInto = currentNode.subNodes.stream().filter(child -> child.name.equals(movingToName)).findFirst();
                if (nodeToMoveInto.isPresent()) {
                    // Instructions are a bit cheeky and sometimes move into a directory without calling ls first!
                    currentNode = nodeToMoveInto.get();
                } else {
                    currentNode = new Node(currentNode, movingToName);
                }

            } else if (isMoveDirectoryUp(line)) {
                currentNode = currentNode.parent;
            } else if (isLs(line)) {
                //do nothing
            } else {
                // Must be output of LS, either a directory or a file.
                if (line.startsWith("dir")) {
                    String nodeName = line.split(" ")[1];
                    currentNode.subNodes.add(new Node(currentNode, nodeName));
                } else {
                    String size = line.split(" ")[0];
                    String fileName = line.split(" ")[1];// unused
                    currentNode.fileSizes.add(Long.parseLong(size));
                }
            }
        }
        return root;
    }

    private class Node {
        private final Node parent;
        private final String name;
        List<Node> subNodes = new ArrayList<>();
        List<Long> fileSizes = new ArrayList<>();

        public Node(Node parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        long getTotalSize() {
            return fileSizes.stream().mapToLong(i -> i).sum() + subNodes.stream().mapToLong(Node::getTotalSize).sum();
        }

        long getTotalSizeRecordingNodesUnderLimit(List<Node> listToRecord, long limit) {
            long thisSize = fileSizes.stream().mapToLong(i -> i).sum();
            long childSizes = subNodes.stream().mapToLong(node -> node.getTotalSizeRecordingNodesUnderLimit(listToRecord, limit)).sum();
            long totalSize = thisSize + childSizes;
            if (totalSize <= limit) {
                listToRecord.add(this);
            }
            return totalSize;
        }

        long getTotalSizeRecordingNodesOverLimit(List<Node> listToRecord, long limit) {
            long thisSize = fileSizes.stream().mapToLong(i -> i).sum();
            long childSizes = subNodes.stream().mapToLong(node -> node.getTotalSizeRecordingNodesOverLimit(listToRecord, limit)).sum();
            long totalSize = thisSize + childSizes;
            if (totalSize >= limit) {
                listToRecord.add(this);
            }
            return totalSize;
        }
    }

    private boolean isMoveIntoDir(String line) {
        return line.startsWith("$ cd") && !line.startsWith("$ cd ..");
    }

    private boolean isMoveDirectoryUp(String line) {
        return line.startsWith("$ cd ..");
    }

    private boolean isLs(String line) {
        return line.startsWith("$ ls");
    }

    private static String getInput() {
        return "$ cd /\n" +
                "$ ls\n" +
                "dir plws\n" +
                "dir pwlbgbz\n" +
                "dir pwtpltr\n" +
                "dir szn\n" +
                "$ cd plws\n" +
                "$ ls\n" +
                "dir ffpzc\n" +
                "dir frcmjzts\n" +
                "92461 nbvnzg\n" +
                "dir phqcg\n" +
                "21621 vqgsglwq\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "48459 dzdfc.vqq\n" +
                "143107 jql.jzl\n" +
                "208330 mmnvqn.hqb\n" +
                "290122 svjvhflz\n" +
                "218008 wjlmgq\n" +
                "$ cd ..\n" +
                "$ cd frcmjzts\n" +
                "$ ls\n" +
                "dir bsltmjz\n" +
                "dir jfzgrbm\n" +
                "$ cd bsltmjz\n" +
                "$ ls\n" +
                "34237 dzdfc.vqq\n" +
                "58741 mdgdhqgw\n" +
                "$ cd ..\n" +
                "$ cd jfzgrbm\n" +
                "$ ls\n" +
                "132811 fcmpng\n" +
                "103661 lgt.swt\n" +
                "173031 vqgsglwq\n" +
                "29134 wprjfg.zbr\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd phqcg\n" +
                "$ ls\n" +
                "955 jgfs.zjw\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd pwlbgbz\n" +
                "$ ls\n" +
                "dir gbg\n" +
                "dir mjzhcwrd\n" +
                "dir njcscpj\n" +
                "dir sphbzn\n" +
                "dir tbgjpphc\n" +
                "55938 tvrfpczc.djm\n" +
                "4840 twd\n" +
                "$ cd gbg\n" +
                "$ ls\n" +
                "287003 fcsjl.bzm\n" +
                "dir wgq\n" +
                "$ cd wgq\n" +
                "$ ls\n" +
                "22963 fcsjl.fcm\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd mjzhcwrd\n" +
                "$ ls\n" +
                "228632 clfnpmbq.zmb\n" +
                "28276 dzdfc.vqq\n" +
                "2982 tdbg.wgn\n" +
                "$ cd ..\n" +
                "$ cd njcscpj\n" +
                "$ ls\n" +
                "dir dqzgqgv\n" +
                "275186 ffpzc\n" +
                "192491 gjnflc.plq\n" +
                "$ cd dqzgqgv\n" +
                "$ ls\n" +
                "70309 dzdfc.vqq\n" +
                "56139 fcsjl\n" +
                "142095 sgwz.cdz\n" +
                "dir snjntth\n" +
                "dir sphbzn\n" +
                "284618 wjlmgq\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "51918 ffpzc\n" +
                "dir vrfgfds\n" +
                "$ cd vrfgfds\n" +
                "$ ls\n" +
                "155233 jlscz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "dir qbzwrrw\n" +
                "dir qwpzn\n" +
                "$ cd qbzwrrw\n" +
                "$ ls\n" +
                "278531 fcsjl.tqj\n" +
                "211591 snjntth.gpd\n" +
                "$ cd ..\n" +
                "$ cd qwpzn\n" +
                "$ ls\n" +
                "174183 vqgsglwq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "185471 bsltmjz.fqz\n" +
                "dir bsvh\n" +
                "dir fvzcs\n" +
                "dir ndw\n" +
                "dir nlml\n" +
                "dir pcbt\n" +
                "286260 zhcmrpvt\n" +
                "$ cd bsvh\n" +
                "$ ls\n" +
                "130814 wjlmgq\n" +
                "$ cd ..\n" +
                "$ cd fvzcs\n" +
                "$ ls\n" +
                "dir cgmv\n" +
                "dir ggzwljr\n" +
                "298241 qvzghdpw.lms\n" +
                "dir snjntth\n" +
                "dir sphbzn\n" +
                "$ cd cgmv\n" +
                "$ ls\n" +
                "46843 dzdfc.vqq\n" +
                "dir lmqcbbm\n" +
                "dir rstcqsmd\n" +
                "215261 snjntth\n" +
                "$ cd lmqcbbm\n" +
                "$ ls\n" +
                "229898 bdmbvgp\n" +
                "25529 ffpzc.stm\n" +
                "16871 lnpjzvg.qlj\n" +
                "$ cd ..\n" +
                "$ cd rstcqsmd\n" +
                "$ ls\n" +
                "289038 zrbbbwng.smf\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ggzwljr\n" +
                "$ ls\n" +
                "198200 bcthn\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "191672 fwp.phf\n" +
                "68229 hzs.zpg\n" +
                "dir pggcwb\n" +
                "222426 qbv.bwj\n" +
                "dir snjntth\n" +
                "155354 vmqcm\n" +
                "$ cd pggcwb\n" +
                "$ ls\n" +
                "154272 fqztwvnv.lvv\n" +
                "dir pdjg\n" +
                "62393 vqgsglwq\n" +
                "dir wjhrtg\n" +
                "$ cd pdjg\n" +
                "$ ls\n" +
                "260644 gvhlrcf\n" +
                "209906 wpls.pbd\n" +
                "$ cd ..\n" +
                "$ cd wjhrtg\n" +
                "$ ls\n" +
                "148640 dljf.zrq\n" +
                "172168 dzdfc.vqq\n" +
                "196203 hjdphcfm\n" +
                "247620 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "37467 ndlshlmj.cjq\n" +
                "257404 snjntth.nsf\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "64082 bfdv.lwv\n" +
                "dir bsltmjz\n" +
                "58942 dzdfc.vqq\n" +
                "dir snjntth\n" +
                "$ cd bsltmjz\n" +
                "$ ls\n" +
                "dir bsqqdr\n" +
                "266007 fcsjl.gfm\n" +
                "dir ffpzc\n" +
                "dir frsmrd\n" +
                "72122 nthnhzwf\n" +
                "286705 wjlmgq\n" +
                "$ cd bsqqdr\n" +
                "$ ls\n" +
                "117496 wcqt\n" +
                "$ cd ..\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "280224 mmnvqn.hqb\n" +
                "105346 vrr\n" +
                "$ cd ..\n" +
                "$ cd frsmrd\n" +
                "$ ls\n" +
                "111509 sphbzn.shz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "177491 mplj\n" +
                "9029 pvbz.jwn\n" +
                "92891 snjntth.zrv\n" +
                "203356 vnnnw.gql\n" +
                "134728 vqgsglwq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ndw\n" +
                "$ ls\n" +
                "241303 bht.rpj\n" +
                "173068 vqgsglwq\n" +
                "$ cd ..\n" +
                "$ cd nlml\n" +
                "$ ls\n" +
                "228982 hzglfpvq.ftt\n" +
                "114981 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd pcbt\n" +
                "$ ls\n" +
                "dir bsltmjz\n" +
                "dir ffpzc\n" +
                "dir fjsjwfg\n" +
                "dir fwm\n" +
                "dir jvwt\n" +
                "227943 tmr.jdc\n" +
                "dir vwpqzdwh\n" +
                "31258 wjlmgq\n" +
                "$ cd bsltmjz\n" +
                "$ ls\n" +
                "177750 bsltmjz.spj\n" +
                "dir ffpzc\n" +
                "dir flrpwfs\n" +
                "138824 mtmdtcpv.cfj\n" +
                "165770 wzqwczj.qwn\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "179645 snjntth.dss\n" +
                "$ cd ..\n" +
                "$ cd flrpwfs\n" +
                "$ ls\n" +
                "60566 wvjq.gmm\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "97847 qzhhtmd.bhw\n" +
                "1197 vqgsglwq\n" +
                "$ cd ..\n" +
                "$ cd fjsjwfg\n" +
                "$ ls\n" +
                "152232 dnsdd.jgz\n" +
                "181301 gsb.wsh\n" +
                "dir jqpb\n" +
                "dir jscbg\n" +
                "dir snjntth\n" +
                "227677 snjntth.vvg\n" +
                "dir sphbzn\n" +
                "75358 vqgsglwq\n" +
                "2589 wjlmgq\n" +
                "$ cd jqpb\n" +
                "$ ls\n" +
                "253403 mmnvqn.hqb\n" +
                "108325 rvq.mrc\n" +
                "$ cd ..\n" +
                "$ cd jscbg\n" +
                "$ ls\n" +
                "dir dtm\n" +
                "dir gsdnz\n" +
                "208269 prh\n" +
                "25977 qdzljgh\n" +
                "169262 vmnq.mth\n" +
                "$ cd dtm\n" +
                "$ ls\n" +
                "80072 gzqnb\n" +
                "$ cd ..\n" +
                "$ cd gsdnz\n" +
                "$ ls\n" +
                "dir dsqzjs\n" +
                "297895 sgwz.cdz\n" +
                "129983 vqgsglwq\n" +
                "$ cd dsqzjs\n" +
                "$ ls\n" +
                "2621 jqrlsf.gzs\n" +
                "164844 snjntth\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "118553 cbhql\n" +
                "dir ffpzc\n" +
                "dir snjntth\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "dir lmn\n" +
                "12104 tvlwn.vhh\n" +
                "$ cd lmn\n" +
                "$ ls\n" +
                "46401 bsltmjz\n" +
                "96888 shrnqhvq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "dir snjntth\n" +
                "dir vlnfhbq\n" +
                "dir wpwl\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "dir ctj\n" +
                "$ cd ctj\n" +
                "$ ls\n" +
                "82485 fcsjl.lfl\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd vlnfhbq\n" +
                "$ ls\n" +
                "250106 qvf\n" +
                "$ cd ..\n" +
                "$ cd wpwl\n" +
                "$ ls\n" +
                "153950 cmsd.rlg\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "dir glgq\n" +
                "$ cd glgq\n" +
                "$ ls\n" +
                "285182 wjlmgq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd fwm\n" +
                "$ ls\n" +
                "251865 ffpzc.qgb\n" +
                "279522 zvvpfqtp\n" +
                "$ cd ..\n" +
                "$ cd jvwt\n" +
                "$ ls\n" +
                "48990 bsltmjz.nzp\n" +
                "219604 ffpzc\n" +
                "69573 mvmdfzr.lwb\n" +
                "$ cd ..\n" +
                "$ cd vwpqzdwh\n" +
                "$ ls\n" +
                "267581 pvcch\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd tbgjpphc\n" +
                "$ ls\n" +
                "255571 dstpcgr.tfq\n" +
                "dir fdbwbrpp\n" +
                "dir gjzwh\n" +
                "dir hjvrtjt\n" +
                "dir rhzczj\n" +
                "292888 sgwz.cdz\n" +
                "dir wlzhr\n" +
                "149395 wnfzrqgz.dtn\n" +
                "$ cd fdbwbrpp\n" +
                "$ ls\n" +
                "dir ffpzc\n" +
                "dir qbrth\n" +
                "51164 qprp\n" +
                "dir slpt\n" +
                "117026 sphbzn\n" +
                "295685 vqgsglwq\n" +
                "dir znmj\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "dir jhnzrdvb\n" +
                "$ cd jhnzrdvb\n" +
                "$ ls\n" +
                "217775 ffpzc.sgw\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd qbrth\n" +
                "$ ls\n" +
                "183969 lpbwgfjv.vcg\n" +
                "47333 wjlmgq\n" +
                "$ cd ..\n" +
                "$ cd slpt\n" +
                "$ ls\n" +
                "32343 tqhtj.jwz\n" +
                "$ cd ..\n" +
                "$ cd znmj\n" +
                "$ ls\n" +
                "55058 mmnvqn.hqb\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd gjzwh\n" +
                "$ ls\n" +
                "dir dvcbcd\n" +
                "202530 dzdfc.vqq\n" +
                "dir fsgz\n" +
                "dir hfrrqq\n" +
                "54897 jlzn.qsn\n" +
                "16097 ldzqsbb.jzl\n" +
                "225078 pswccrd\n" +
                "dir rqqmldw\n" +
                "292464 rzrdhbp.vld\n" +
                "dir ssqbqqp\n" +
                "dir zsztqrc\n" +
                "$ cd dvcbcd\n" +
                "$ ls\n" +
                "187837 dzdfc.vqq\n" +
                "dir jlwtvf\n" +
                "dir jnjvshs\n" +
                "164053 nrf.fqd\n" +
                "5665 tlp.jmt\n" +
                "13801 wjlmgq\n" +
                "$ cd jlwtvf\n" +
                "$ ls\n" +
                "219985 sphbzn.dvj\n" +
                "$ cd ..\n" +
                "$ cd jnjvshs\n" +
                "$ ls\n" +
                "dir bsltmjz\n" +
                "dir nrpm\n" +
                "$ cd bsltmjz\n" +
                "$ ls\n" +
                "152972 qgdqj\n" +
                "$ cd ..\n" +
                "$ cd nrpm\n" +
                "$ ls\n" +
                "18509 wjlmgq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd fsgz\n" +
                "$ ls\n" +
                "224576 mmnvqn.hqb\n" +
                "$ cd ..\n" +
                "$ cd hfrrqq\n" +
                "$ ls\n" +
                "dir bwgsnfvb\n" +
                "dir fcsjl\n" +
                "294608 ffpzc.gvm\n" +
                "136880 qjcgtw\n" +
                "dir sphbzn\n" +
                "$ cd bwgsnfvb\n" +
                "$ ls\n" +
                "29735 dzdfc.vqq\n" +
                "dir wstmzfml\n" +
                "$ cd wstmzfml\n" +
                "$ ls\n" +
                "158447 bnvhbvvc.nrt\n" +
                "59889 jclclgd\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd fcsjl\n" +
                "$ ls\n" +
                "138297 ffpzc.szw\n" +
                "$ cd ..\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "dir wqdths\n" +
                "$ cd wqdths\n" +
                "$ ls\n" +
                "8326 cgvtw.jpz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd rqqmldw\n" +
                "$ ls\n" +
                "226757 dzdfc.vqq\n" +
                "115055 mwb.btc\n" +
                "dir qpts\n" +
                "298524 sgwz.cdz\n" +
                "$ cd qpts\n" +
                "$ ls\n" +
                "60860 bsltmjz.frp\n" +
                "dir fcsjl\n" +
                "94904 sgwz.cdz\n" +
                "dir wnmqqspz\n" +
                "$ cd fcsjl\n" +
                "$ ls\n" +
                "25082 mmnvqn.hqb\n" +
                "$ cd ..\n" +
                "$ cd wnmqqspz\n" +
                "$ ls\n" +
                "165529 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ssqbqqp\n" +
                "$ ls\n" +
                "192477 pvrgm\n" +
                "$ cd ..\n" +
                "$ cd zsztqrc\n" +
                "$ ls\n" +
                "254053 lht.htn\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd hjvrtjt\n" +
                "$ ls\n" +
                "189942 fwps\n" +
                "$ cd ..\n" +
                "$ cd rhzczj\n" +
                "$ ls\n" +
                "36502 bmtfr\n" +
                "dir ffjz\n" +
                "35148 nctfhmzm.vsz\n" +
                "dir qdgjzcz\n" +
                "208196 rwql\n" +
                "79863 sgwz.cdz\n" +
                "dir snjntth\n" +
                "$ cd ffjz\n" +
                "$ ls\n" +
                "dir grsvhwm\n" +
                "$ cd grsvhwm\n" +
                "$ ls\n" +
                "50231 fwj.rdv\n" +
                "dir snjntth\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "dir dtbgb\n" +
                "$ cd dtbgb\n" +
                "$ ls\n" +
                "150245 vdflm.lmq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd qdgjzcz\n" +
                "$ ls\n" +
                "222389 dzdfc.vqq\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "56794 mmnvqn.hqb\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd wlzhr\n" +
                "$ ls\n" +
                "116628 bsltmjz\n" +
                "60122 jqpbsgnr.fgb\n" +
                "252605 lfss\n" +
                "300065 qwjdl.vhr\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd pwtpltr\n" +
                "$ ls\n" +
                "dir dplsvrhl\n" +
                "140951 gwtfzqvd.znb\n" +
                "dir jbvdb\n" +
                "dir jst\n" +
                "dir qhjv\n" +
                "dir snjntth\n" +
                "$ cd dplsvrhl\n" +
                "$ ls\n" +
                "272101 fcsjl\n" +
                "dir ffpzc\n" +
                "58852 mmnvqn.hqb\n" +
                "dir mnhntjz\n" +
                "91899 sgwz.cdz\n" +
                "228077 snjntth.btv\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "5499 bsltmjz\n" +
                "dir qmfwpjhl\n" +
                "dir rsrb\n" +
                "dir wgt\n" +
                "$ cd qmfwpjhl\n" +
                "$ ls\n" +
                "300362 dzdfc.vqq\n" +
                "$ cd ..\n" +
                "$ cd rsrb\n" +
                "$ ls\n" +
                "252983 dzdfc.vqq\n" +
                "107744 ltssrgqb.zvj\n" +
                "214895 rhglgcwr.wpw\n" +
                "249727 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd wgt\n" +
                "$ ls\n" +
                "141984 dzdfc.vqq\n" +
                "194686 mmnvqn.hqb\n" +
                "258023 pgr\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd mnhntjz\n" +
                "$ ls\n" +
                "dir bdvght\n" +
                "dir jprwchh\n" +
                "dir snjntth\n" +
                "$ cd bdvght\n" +
                "$ ls\n" +
                "243166 vpsvjdq.wsn\n" +
                "$ cd ..\n" +
                "$ cd jprwchh\n" +
                "$ ls\n" +
                "178943 bmpc.bjb\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "dir nlbm\n" +
                "dir zjmjntff\n" +
                "$ cd nlbm\n" +
                "$ ls\n" +
                "33050 fcsjl.rcc\n" +
                "dir sphbzn\n" +
                "17446 wjlmgq\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "214563 prrfhff.pbp\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd zjmjntff\n" +
                "$ ls\n" +
                "82134 sgwz.cdz\n" +
                "52203 vrtlgdq.crp\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd jbvdb\n" +
                "$ ls\n" +
                "dir wmtjh\n" +
                "$ cd wmtjh\n" +
                "$ ls\n" +
                "dir ggvwn\n" +
                "$ cd ggvwn\n" +
                "$ ls\n" +
                "192285 spqvmf.sdh\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd jst\n" +
                "$ ls\n" +
                "dir bsltmjz\n" +
                "212740 dzdfc.vqq\n" +
                "dir gncztvtb\n" +
                "dir jsqjcqnt\n" +
                "286257 jvs\n" +
                "36654 sdcsm.mbg\n" +
                "192040 sgwz.cdz\n" +
                "dir tbqphzb\n" +
                "dir vdcqgts\n" +
                "285843 wjlmgq\n" +
                "$ cd bsltmjz\n" +
                "$ ls\n" +
                "215705 snjntth.gpv\n" +
                "213665 wjlmgq\n" +
                "$ cd ..\n" +
                "$ cd gncztvtb\n" +
                "$ ls\n" +
                "229298 vqgsglwq\n" +
                "$ cd ..\n" +
                "$ cd jsqjcqnt\n" +
                "$ ls\n" +
                "dir bsltmjz\n" +
                "dir fcsjl\n" +
                "dir ffpzc\n" +
                "dir sphbzn\n" +
                "70864 vqgsglwq\n" +
                "$ cd bsltmjz\n" +
                "$ ls\n" +
                "14981 pqzffzjc\n" +
                "$ cd ..\n" +
                "$ cd fcsjl\n" +
                "$ ls\n" +
                "140328 jwhczwbc\n" +
                "$ cd ..\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "213650 mmnvqn.hqb\n" +
                "$ cd ..\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "dir psmtphhq\n" +
                "dir sphbzn\n" +
                "$ cd psmtphhq\n" +
                "$ ls\n" +
                "dir ffpzc\n" +
                "123131 tzgwd\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "49737 cfngvmd\n" +
                "dir gcnrp\n" +
                "172799 gmd.cwl\n" +
                "dir llnztjf\n" +
                "dir nbqs\n" +
                "79661 rrqz\n" +
                "$ cd gcnrp\n" +
                "$ ls\n" +
                "283 vqnrgl.vwp\n" +
                "$ cd ..\n" +
                "$ cd llnztjf\n" +
                "$ ls\n" +
                "63263 tjhm.bwh\n" +
                "$ cd ..\n" +
                "$ cd nbqs\n" +
                "$ ls\n" +
                "dir vssmq\n" +
                "$ cd vssmq\n" +
                "$ ls\n" +
                "88980 dzdfc.vqq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd sphbzn\n" +
                "$ ls\n" +
                "20140 fcsjl.zrs\n" +
                "260579 snjntth\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd tbqphzb\n" +
                "$ ls\n" +
                "93470 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd vdcqgts\n" +
                "$ ls\n" +
                "223564 dzdfc.vqq\n" +
                "dir ffpzc\n" +
                "dir gwhfgwf\n" +
                "dir nbjtqnng\n" +
                "dir snjntth\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "42813 qwwmw.nmt\n" +
                "$ cd ..\n" +
                "$ cd gwhfgwf\n" +
                "$ ls\n" +
                "59918 jvfv.mpm\n" +
                "dir mjl\n" +
                "211039 pcwl\n" +
                "$ cd mjl\n" +
                "$ ls\n" +
                "13004 pgjb.tpq\n" +
                "195995 tms.fjz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd nbjtqnng\n" +
                "$ ls\n" +
                "107058 dzdfc.vqq\n" +
                "dir ldrsd\n" +
                "111631 vqgsglwq\n" +
                "104599 wbzmdljw.tjq\n" +
                "155747 wjlmgq\n" +
                "$ cd ldrsd\n" +
                "$ ls\n" +
                "107439 jvjm\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "242680 fgrt.gng\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd qhjv\n" +
                "$ ls\n" +
                "dir bmnm\n" +
                "68453 hjjpdgn.hwl\n" +
                "dir sjlbj\n" +
                "dir vqnrj\n" +
                "$ cd bmnm\n" +
                "$ ls\n" +
                "1238 vqgsglwq\n" +
                "$ cd ..\n" +
                "$ cd sjlbj\n" +
                "$ ls\n" +
                "44239 wzzbtmrz.gml\n" +
                "$ cd ..\n" +
                "$ cd vqnrj\n" +
                "$ ls\n" +
                "3286 bsltmjz.qlc\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "130833 blm.wmt\n" +
                "dir snjntth\n" +
                "dir tcnmbcgg\n" +
                "218869 wjlmgq\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "dir snmrdfbt\n" +
                "$ cd snmrdfbt\n" +
                "$ ls\n" +
                "281025 bzrsds.lfg\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd tcnmbcgg\n" +
                "$ ls\n" +
                "194998 fcsjl\n" +
                "dir qdrmpqgn\n" +
                "dir rzqd\n" +
                "dir tcsds\n" +
                "$ cd qdrmpqgn\n" +
                "$ ls\n" +
                "165713 qmzgt.tnc\n" +
                "$ cd ..\n" +
                "$ cd rzqd\n" +
                "$ ls\n" +
                "dir cwhnmlv\n" +
                "57819 fcsjl\n" +
                "246864 pjnzdvd.gjm\n" +
                "$ cd cwhnmlv\n" +
                "$ ls\n" +
                "287539 mmnvqn.hqb\n" +
                "215636 pbnjt.zbn\n" +
                "124638 sqd\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd tcsds\n" +
                "$ ls\n" +
                "78812 gfmgb.wqj\n" +
                "218987 hnhfvz.dln\n" +
                "209640 mzzhqlq.zqp\n" +
                "102492 nml.ppg\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd szn\n" +
                "$ ls\n" +
                "dir fcsjl\n" +
                "dir snjntth\n" +
                "dir zjbp\n" +
                "$ cd fcsjl\n" +
                "$ ls\n" +
                "158019 jsv.pmz\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "229510 dfvpvp\n" +
                "191061 fgplbptq.jlt\n" +
                "dir lfb\n" +
                "234911 lfsrwr.wcb\n" +
                "dir lrfcgzl\n" +
                "48031 stbbw\n" +
                "219691 vqgsglwq\n" +
                "dir zshh\n" +
                "$ cd lfb\n" +
                "$ ls\n" +
                "dir btj\n" +
                "99591 dhrjbvvg.gwm\n" +
                "137224 dzdfc.vqq\n" +
                "201972 jtzmqsvj.wnd\n" +
                "9704 mmnvqn.hqb\n" +
                "dir pwg\n" +
                "200308 snjntth.css\n" +
                "dir wcmhcfm\n" +
                "dir zwhvmln\n" +
                "$ cd btj\n" +
                "$ ls\n" +
                "dir rnbzdfgn\n" +
                "51799 wdhsm\n" +
                "dir wvf\n" +
                "$ cd rnbzdfgn\n" +
                "$ ls\n" +
                "117095 bsltmjz.tlv\n" +
                "$ cd ..\n" +
                "$ cd wvf\n" +
                "$ ls\n" +
                "dir ffpzc\n" +
                "dir ncbmgpsc\n" +
                "dir wtwrmjnt\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "249919 lsth.fmf\n" +
                "$ cd ..\n" +
                "$ cd ncbmgpsc\n" +
                "$ ls\n" +
                "147899 dzdfc.vqq\n" +
                "$ cd ..\n" +
                "$ cd wtwrmjnt\n" +
                "$ ls\n" +
                "252366 pvpdv.jwz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd pwg\n" +
                "$ ls\n" +
                "280845 fcsjl.fjz\n" +
                "44300 sgwz.cdz\n" +
                "dir snjntth\n" +
                "229605 vqgsglwq\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "2053 pflvsnzs\n" +
                "143522 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd wcmhcfm\n" +
                "$ ls\n" +
                "229329 qsznhwlw.vjg\n" +
                "$ cd ..\n" +
                "$ cd zwhvmln\n" +
                "$ ls\n" +
                "dir ffpzc\n" +
                "dir tjjzbf\n" +
                "dir wzcq\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "dir ncnj\n" +
                "37497 vqgsglwq\n" +
                "$ cd ncnj\n" +
                "$ ls\n" +
                "40920 htbjhjq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd tjjzbf\n" +
                "$ ls\n" +
                "47522 mczn.spd\n" +
                "$ cd ..\n" +
                "$ cd wzcq\n" +
                "$ ls\n" +
                "56662 ffpzc.vwp\n" +
                "dir snjntth\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "117276 wjlmgq\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd lrfcgzl\n" +
                "$ ls\n" +
                "267485 rsjmpph.qqz\n" +
                "$ cd ..\n" +
                "$ cd zshh\n" +
                "$ ls\n" +
                "dir ffpzc\n" +
                "dir gmn\n" +
                "dir snjntth\n" +
                "150048 tgtlh\n" +
                "32020 thfr\n" +
                "72152 vqgsglwq\n" +
                "$ cd ffpzc\n" +
                "$ ls\n" +
                "dir snjntth\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "224945 dpfpz\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd gmn\n" +
                "$ ls\n" +
                "238996 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd snjntth\n" +
                "$ ls\n" +
                "86775 dzdfc.vqq\n" +
                "19560 vshcmjj\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd ..\n" +
                "$ cd zjbp\n" +
                "$ ls\n" +
                "dir fcsjl\n" +
                "41522 nlvpb.fpf\n" +
                "dir nmtjtd\n" +
                "$ cd fcsjl\n" +
                "$ ls\n" +
                "276802 fcsjl.psm\n" +
                "197934 sgwz.cdz\n" +
                "$ cd ..\n" +
                "$ cd nmtjtd\n" +
                "$ ls\n" +
                "47477 dvqmqlgw.ths\n" +
                "51081 vqgsglwq";
    }
}
