# JinrouBBSAnalysisChiritori
人狼BBSから取れる情報をデータベース化するプログラム「ちりとり」
データベースはSQLiteを利用

テーブル情報
-----------------------
villageTable      : 村情報の入るテーブル．一つの村につき一つある
  villageNumber : 村ごとに付けられる番号
  villageName   : 村ごとに付けられる名前
  updateTime    : 村の一日が切り替わる時間
  people        : 村の参加人数
  
dateTable         : 一日の情報が入るテーブル　二日目以降からゲーム終了日まで
  villageNumber : 村ごとに付けられる番号
  date          : 村の日数（０はプロローグ）
  divination    : その日，占われた人
  defend        : その日，守られた人
  attack        : その日，襲われた人
  
serifTable        : セリフ情報が入るテーブル　通常発言以外はスレッド番号が振られないため，uniqueNumberを追加した
  villageNumber : 村ごとに付けられる番号
  uniqueNumber  : すべての発言を含む連番
  date          : そのセリフがされた日
  threadNumber  : 通常発言に付けられる番号
  color         : "white"は通常発言"red"は狼の発言"blue"は死者の発言"gray"はつぶやき
  player        : 発言者
  contribution  : 投稿時間
  serif         : セリフ内容
postTable         : 配役情報　狼，村人，占い師，狩人，霊能者
  villageNumber : 村ごとに付けられる番号
  player        : プレイヤー名
  post          : 担当した役職
  
voteTable         : 投票先情報　三日目からエピローグまで
 villageNumber  : 村ごとに付けられる番号
 date           : 投票があった日
 player         : 投票者
 vote           : 投票先
 
--------------------
