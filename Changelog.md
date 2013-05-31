## tilskudsblanketimporter 4.2
*  The old primary keys where not unique so new was choosen or created - NSPSUPPORT-182
*  ModifiedDate now gets correctly updated - NSPSUPPORT-181

The follwing tables must be emptied before upgrading to 4.2
Tilskudsblanket
TilskudForhoejetTakst
TilskudsblanketTerminal
TilskudsblanketKroniker
TilskudsblanketForhoejet
TilskudsblanketEnkelt

## tilskudsblanketimporter 4.3
See release-4.2
+Fixed typo in integration tests

## tilskudsblanketimporter 4.4
*  Opdateret SDM4 depencencies
*  SDM-5 SLA-log fra SDM4-importere følger ikke standarden
*  Tilføjet kopi register view, så kopi register service maps nu bliver oprettet automatisk
