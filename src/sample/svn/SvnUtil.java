package sample.svn;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;
import org.tmatesoft.svn.util.ISVNDebugLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-22 14:35
 */
public class SvnUtil {

    //声明SVN客户端管理类
    private static SVNClientManager ourClientManager;
    //svn地址
    private static String url = "http://192.168.1.16/svn/soft/dev/java/baq/baq_4/chasstage/code/trunk/chasstage";
    private static String USERNAME = "wutongli";
    private static String PASSWORD = "wtl123";

    private static SVNRepository repository = null;

    public static void setSvnLogInfo(String svnUrl,String username,String password){
        SvnUtil.url = svnUrl;
        SvnUtil.USERNAME = username;
        SvnUtil.PASSWORD = password;
    }

    public static void downSvn() {
        try {
            SVNURL repositoryURL = null;
            try {
                repositoryURL = SVNURL.parseURIEncoded(url);
            } catch (SVNException e) {
                //
            }
            String name = USERNAME;
            String password = PASSWORD;
            ISVNOptions options = SVNWCUtil.createDefaultOptions(true);

            //实例化客户端管理类
            ourClientManager = SVNClientManager.newInstance(
                    (DefaultSVNOptions) options, name, password);

            //要把版本库的内容check out到的目录
            File wcDir = new File("D:\\Users\\htk\\Desktop\\svntest");

            //通过客户端管理类获得updateClient类的实例。
            SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
            /*
             * sets externals not to be ignored during the checkout
             */
            updateClient.setIgnoreExternals(false);

            //执行check out 操作，返回工作副本的版本号。
            SVNRevision svnRevision = SVNRevision.create(10078);
            long workingVersion = updateClient
                    .doCheckout(repositoryURL, wcDir, SVNRevision.HEAD, svnRevision, SVNDepth.INFINITY, false);
            System.out.println("把版本：" + workingVersion + " check out 到目录：" + wcDir + "中。");
            ISVNDebugLog debugLog = updateClient.getDebugLog();
            System.out.println(debugLog);
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    public static List<String> filterCommitHistory(long startRevision,long endRevision) throws Exception {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException e) {
            e.printStackTrace();
        }
        // 身份验证
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(USERNAME, PASSWORD);
        repository.setAuthenticationManager(authManager);

        // 过滤条件
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
   /*     final Date begin = format.parse("2019-04-13");
        final Date end = format.parse("2022-05-14");*/
        final String author = "";  //过滤提交人
        final List<String> history = new ArrayList<String>();
        //String[] 为过滤的文件路径前缀，为空表示不进行过滤
        repository.log(new String[]{""},
                startRevision,
                endRevision,
                true,
                true,
                new ISVNLogEntryHandler() {
                    @Override
                    public void handleLogEntry(SVNLogEntry svnlogentry)
                            throws SVNException {
                        //依据提交时间进行过滤
                      /*  if (svnlogentry.getDate().after(begin)
                                && svnlogentry.getDate().before(end)) {*/
                        // 依据提交人过滤
                        if (!"".equals(author)) {
                            if (author.equals(svnlogentry.getAuthor())) {
                                fillResult(svnlogentry);
                            }
                        } else {
                            fillResult(svnlogentry);
                        }
                        /*}*/
                    }

                    public void fillResult(SVNLogEntry svnlogentry) {
                        //getChangedPaths为提交的历史记录MAP key为文件名，value为文件详情
                        Map<String, SVNLogEntryPath> changedPaths = svnlogentry.getChangedPaths();
                        Set<String> strings = svnlogentry.getChangedPaths().keySet();
                        for (String str : strings) {
                            SVNLogEntryPath svnLogEntryPath = changedPaths.get(str);
                            char type = svnLogEntryPath.getType();
                            if('A' == type || 'M' == type){
                                history.add(svnLogEntryPath.getPath());
                            }
                        }
                    }
                });
        for (String path : history) {
            System.out.println(path);
        }
        return history;
    }

    public static void main(String[] args) throws Exception {
//        downSvn();
        filterCommitHistory(10008,10078);
//        ISVNDebugLog debugLog = ourClientManager.getDebugLog();
    }
}
