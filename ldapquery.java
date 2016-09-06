package ldapquery;

import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class ldapQuery {
    
    static DirContext ldapContext;
    
    public static void main(String[] args) {
        
        try {
            Hashtable<String, Object> ldapEnv = new Hashtable<String, Object>(11);
            
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL, "ldap://yourserver.hu:389");
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn=your_user, ou=your_ou, dc=yourdomain, dc=com");
            ldapEnv.put(Context.SECURITY_CREDENTIALS, "pw");
            ldapContext = new InitialDirContext(ldapEnv);
            
            SearchControls searchCtls = new SearchControls();
            String returnedAtts[]={"sn", "givenName", "samAccountName"};
            searchCtls.setReturningAttributes(returnedAtts);
            
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "(&(objectClass=user))";
            String searchBase = "ou=EAUsers,dc=europ-assistance,dc=hu";
            int totalResults = 0;
            
            NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);
            
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult)answer.next();
                totalResults++;
                System.out.println(">>>" + sr.getName());
                Attributes attrs = sr.getAttributes();
                System.out.println(">>>>>>" + attrs.get("samAccountName"));
            }
            
            System.out.println("Total results: " + totalResults);
            ldapContext.close();
            
        } catch (AuthenticationException e) {
            
            System.out.println(" Search error: Username and password mismatch");
            System.out.println(" # Exception details: " + e);
            System.out.println();
            e.printStackTrace();
            System.exit(-1);
            
        } catch (Exception e) {
            
            System.out.println(" Exception caught: " + e);
            e.printStackTrace();
            
        }
    }
    
}
