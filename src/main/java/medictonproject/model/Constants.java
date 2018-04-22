package medictonproject.model;

public interface Constants {
  int PAGE_SIZE_ANNOUNCEMENTS_USER = 5;
  int PAGE_SIZE_ANNOUNCEMENTS_ADMIN = 5;
  int PAGE_SIZE_CONVERSATIONS = 5;
  int PAGE_SIZE_EQUIPMENT = 20;
  int PAGE_SIZE_SURVEYS = 20;
  int PAGE_SIZE_USERS = 20;
  int PAGE_SIZE_PROTOCOLS = 20;

  final String intuoUsername = "TST";
  final String intuoPassword = "Testuser01";
  final String urlLogin = "http://192.168.1.21/Webservice/Service3auth.asmx/Login";
  final String urlGetView = "http://192.168.1.21/Webservice/Service3auth.asmx/GetView";
  final String urlGetBindingView = "http://192.168.1.21/Webservice/Service3auth.asmx/GetBindingView";
  final String view745_strXMLDefinitionOfRestriction = "<Element type=\"LogicalOperator\"><SubElements><ElementCollection><Item><Element type=\"LogicalOperator\"><SubElements><ElementCollection><Item><Element type=\"ColumnName\"><AdditionalData columnName=\"isActive\" displayName=\"Aktivní\" type=\"4\" /></Element></Item><Item><Element type=\"ColumnName\"><AdditionalData columnName=\"isINPO\" displayName=\"INPO\" type=\"4\" /></Element></Item></ElementCollection></SubElements><AdditionalData Operator=\"AND\" /></Element></Item><Item><Element type=\"Comparison\"><SubElements><ElementCollection><Item><Element type=\"ColumnName\"><AdditionalData columnName=\"mail\" displayName=\"Email1\" type=\"1\" /></Element></Item><Item><Element type=\"Constant\"><AdditionalData><anyType xmlns:q1=\"http://www.w3.org/2001/XMLSchema\" d11p1:type=\"q1:string\" xmlns:d11p1=\"http://www.w3.org/2001/XMLSchema-instance\">%s</anyType></AdditionalData></Element></Item></ElementCollection></SubElements><AdditionalData Operator=\"Equal\" /></Element></Item></ElementCollection></SubElements><AdditionalData Operator=\"AND\" /></Element>";
  final String view745_strXMLDefinitionOfRestrictionByICO = "<Element type=\"Comparison\"><SubElements><ElementCollection><Item><Element type=\"ColumnName\"><AdditionalData columnName=\"{pb:Company}.ico\" displayName=\"-Organizace.IČO\" type=\"1\" /></Element></Item><Item><Element type=\"Constant\"><AdditionalData><anyType xmlns:q1=\"http://www.w3.org/2001/XMLSchema\" d7p1:type=\"q1:string\" xmlns:d7p1=\"http://www.w3.org/2001/XMLSchema-instance\">%s</anyType></AdditionalData></Element></Item></ElementCollection></SubElements><AdditionalData Operator=\"Like\" /></Element>";

}
