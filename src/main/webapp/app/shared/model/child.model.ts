import { IParent } from 'app/shared/model/parent.model';

export interface IChild {
  id?: number;
  name?: string;
  parent?: IParent;
}

export class Child implements IChild {
  constructor(public id?: number, public name?: string, public parent?: IParent) {}
}
