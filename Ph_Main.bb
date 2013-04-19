Include "Ph_Object.bb"
Include "Ph_Collision.bb"


Function MainPhysicTick(t#)
	Ph_DoCollision(t,0.0001, 0.5)
	Local obj.Ph_Object
	For obj = Each Ph_Object
		Ph_DoTick(obj, t)
	Next
	;Ph_DoTick(Testobject2, (MilliSecs()-LastTime)/1000)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D