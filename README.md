# JinrouBBSAnalysisChiritori
人狼BBSから取れる情報をデータベース化するプログラム「ちりとり」
データベースはSQLiteを利用

テーブル情報
-----------------------
villageTable      : 村情報の入るテーブル．一つの村につき一つある<br>
  villageNumber : 村ごとに付けられる番号<br>
  villageName   : 村ごとに付けられる名前<br>
  updateTime    : 村の一日が切り替わる時間<br>
  people        : 村の参加人数<br>
  <br>
dateTable         : 一日の情報が入るテーブル　二日目以降からゲーム終了日まで<br>
  villageNumber : 村ごとに付けられる番号<br>
  date          : 村の日数（０はプロローグ）<br>
  divination    : その日，占われた人<br>
  defend        : その日，守られた人<br>
  attack        : その日，襲われた人<br>
  <br>
serifTable        : セリフ情報が入るテーブル　通常発言以外はスレッド番号が振られないため，uniqueNumberを追加した<br>
  villageNumber : 村ごとに付けられる番号<br>
  uniqueNumber  : すべての発言を含む連番<br>
  date          : そのセリフがされた日<br>
  threadNumber  : 通常発言に付けられる番号<br>
  color         : "white"は通常発言"red"は狼の発言"blue"は死者の発言"gray"はつぶやき<br>
  player        : 発言者<br>
  contribution  : 投稿時間<br>
  serif         : セリフ内容<br>
postTable         : 配役情報　狼，村人，占い師，狩人，霊能者<br>
  villageNumber : 村ごとに付けられる番号<br>
  player        : プレイヤー名<br>
  post          : 担当した役職<br>
  <br>
voteTable         : 投票先情報　三日目からエピローグまで<br>
 villageNumber  : 村ごとに付けられる番号<br>
 date           : 投票があった日<br>
 player         : 投票者<br>
 vote           : 投票先<br>
 <br>
--------------------
