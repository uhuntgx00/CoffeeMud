package com.planet_ink.coffee_mud.WebMacros;

import com.planet_ink.miniweb.interfaces.*;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.util.*;

/* 
   Copyright 2000-2013 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
@SuppressWarnings({"unchecked","rawtypes"})
public class FileNext extends StdWebMacro
{
	public String name(){return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);}
	public boolean isAdminMacro()	{return true;}

	public String runMacro(HTTPRequest httpReq, String parm)
	{
		java.util.Map<String,String> parms=parseParms(parm);
		String path=httpReq.getUrlParameter("PATH");
		if(path==null) path="";
		String last=httpReq.getUrlParameter("FILE");
		MOB M = Authenticate.getAuthenticatedMob(httpReq);
		if(M==null) return "[authentication error]";
		if(parms.containsKey("RESET"))
		{	
			if(last!=null) httpReq.removeUrlParameter("FILE");
			return "";
		}
 		CMFile directory=new CMFile(path,M,false);
		XVector fileList=new XVector();
		if((directory.canRead())&&(directory.isDirectory()))
		{
			httpReq.addFakeUrlParameter("PATH",directory.getVFSPathAndName());
			CMFile[] dirs=CMFile.getFileList(path,"",M,false,true);
			for(int d=0;d<dirs.length;d++)
				fileList.addElement(dirs[d].getName());
			
		}
		fileList.sort();
		String lastID="";
		for(int q=0;q<fileList.size();q++)
		{
			String name=(String)fileList.elementAt(q);
			if((last==null)||((last.length()>0)&&(last.equals(lastID))&&(!name.equals(lastID))))
			{
				httpReq.addFakeUrlParameter("FILE",name);
				return "";
			}
			lastID=name;
		}
		httpReq.addFakeUrlParameter("FILE","");
		if(parms.containsKey("EMPTYOK"))
			return "<!--EMPTY-->";
		return " @break@";
	}

}
