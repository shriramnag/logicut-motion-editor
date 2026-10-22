function doPost(e) {
  var sheet = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();
  if (sheet.getLastRow() === 0) {
    sheet.appendRow(["Timestamp", "Name", "Email_Or_Phone", "Plan_Tier", "Action", "Project_Summary"]);
  }
  var data = JSON.parse(e.postData.contents);
  sheet.appendRow([
    new Date(),
    data.name || "Guest User",
    data.email || "-",
    data.plan || "Free Starter",
    data.action || "Studio Event",
    data.projectSummary || ""
  ]);
  return ContentService.createTextOutput(
    JSON.stringify({ status: "success" })
  ).setMimeType(ContentService.MimeType.JSON);
}
