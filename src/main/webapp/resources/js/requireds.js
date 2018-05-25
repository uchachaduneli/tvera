function caseRequredFields(formVar) {
  if (formVar.name.$error.required) {
    errorMsg('დასახელების მითითება სავალდებულოა');
    return false;
  }
  if (formVar.number.$error.required) {
    errorMsg('საქმის ნომრის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.boardId.$error.required) {
    errorMsg('კოლეგიის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.judgeId.$error.required) {
    errorMsg('მოსამართლის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.startdate.$error.required && $("input[name=startdate]").val().length === 0) {
    errorMsg('საქმის დაწყების თარიღის მითითება სავალდებულოა');
    return false;
  }

  if (formVar.litigationSubjectId.$error.required) {
    errorMsg(' დავის საგნის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.litigationPrice.$error.required) {
    errorMsg('დავის საგნის ღირებულების მითითება სავალდებულოა');
    return false;
  }
  if (formVar.litigationDescription.$error.required) {
    errorMsg(' დავის შინაარსის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.courtInstanceId.$error.required) {
    errorMsg(' სასამართლო ინსტანციის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.endResultId.$error.required) {
    errorMsg('საქმის დამთავრების შედეგის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.courtId.$error.required) {
    errorMsg(' სასამართლოს მითითება სავალდებულოა');
    return false;
  }
  if (formVar.ministryStatus.$error.required) {
    errorMsg(' სამინისტროს სტატუსის მითითება სავალდებულოა');
    return false;
  }
  if (formVar.statusId.$error.required) {
    errorMsg(' სტატუსის მითითება სავალდებულოა');
    return false;
  }
  return true;
}