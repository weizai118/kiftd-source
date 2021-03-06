package kohgylw.kiftd.server.service.impl;

import kohgylw.kiftd.server.service.*;
import org.springframework.stereotype.*;

import com.google.gson.Gson;

import kohgylw.kiftd.server.mapper.*;
import javax.annotation.*;
import javax.servlet.http.*;
import kohgylw.kiftd.server.pojo.*;
import kohgylw.kiftd.server.enumeration.*;
import java.util.*;
import kohgylw.kiftd.server.model.*;
import kohgylw.kiftd.server.util.*;

@Service
public class PlayAudioServiceImpl implements PlayAudioService
{
    @Resource
    private NodeMapper fm;
    @Resource
    private AudioInfoUtil aiu;
    @Resource
    private Gson gson;
    
    private AudioInfoList foundAudios(final HttpServletRequest request) {
        final String fileId = request.getParameter("fileId");
        if (fileId != null && fileId.length() > 0) {
            final String account = (String)request.getSession().getAttribute("ACCOUNT");
            if (ConfigureReader.instance().authorized(account, AccountAuth.DOWNLOAD_FILES)) {
                final List<Node> blocks = (List<Node>)this.fm.queryBySomeFolder(fileId);
                return this.aiu.transformToAudioInfoList(blocks, fileId);
            }
        }
        return null;
    }
    
    public String getAudioInfoListByJson(final HttpServletRequest request) {
        final AudioInfoList ail = this.foundAudios(request);
        if (ail != null) {
            return gson.toJson((Object)ail);
        }
        return "ERROR";
    }
}
